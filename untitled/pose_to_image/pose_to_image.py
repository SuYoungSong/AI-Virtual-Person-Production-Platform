from diffusers import StableDiffusionControlNetPipeline, ControlNetModel
import torch
from controlnet_aux import OpenposeDetector
from diffusers.utils import load_image
from diffusers import DDPMScheduler,UniPCMultistepScheduler
import os
import sys,io
from pose_to_image.llmModel import prompt_engineering
import logging
import warnings

# 해당 종류의 경고를 무시
warnings.filterwarnings("ignore", category=FutureWarning)

# Set the log level to only show ERROR and CRITICAL messages
logging.basicConfig(level=logging.ERROR)

def pose_to_image(model_version,user_path, prompt_user, negative_prompt_user, model_name = "majicMIX-realistic-v5", pose_image_path = "pose.png", num_steps_user = 20, guidance_scale_users = 12):

    # sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')
    # sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding='utf-8')

    # 이미지 저장공간 생성
    os.makedirs(f"repository/image/{user_path}", exist_ok=True)
    # os.makedirs(f"front_server/endproject/src/main/resources/apiResult/image/{user_path}", exist_ok=True)

    ## controlNet 로드
    openpose = OpenposeDetector.from_pretrained('lllyasviel/ControlNet')

    ## 원본 이미지 설정 < 포즈 추출할때 사용 >
    # image = load_image("pose.jpg")

    ## 포즈 이미지 설정
    # pose_image = load_image("pose.png")
    pose_image = load_image(pose_image_path)

    ## 사용할 version 설정
    if model_version == "2.1":
        sd_model = "pose_to_image/stable-diffusion-2-1-base"
        controlnet_model = "thibaud/controlnet-sd21-openpose-diffusers"
    else:
        sd_model = f"pose_to_image/{model_name}"
        # sd_model = "pose_to_image/majicMIX-realistic-v5"
        # sd_model = "pose_to_image/realdosmix"
        # sd_model = "pose_to_image/stable-diffusion-v1-5"
        controlnet_model = "fusing/stable-diffusion-v1-5-controlnet-openpose"

    controlnet = ControlNetModel.from_pretrained(
        controlnet_model,
        torch_dtype=torch.float16
    )
    pipe = StableDiffusionControlNetPipeline.from_pretrained(
        sd_model,
        controlnet=controlnet,
        # safety_checker=None,
        torch_dtype=torch.float16
    ).to("cuda")



    pipe.enable_model_cpu_offload()

    ## 스케줄러
    #pipe.scheduler = DDPMScheduler.from_config(pipe.scheduler.config)
    pipe.scheduler = UniPCMultistepScheduler.from_config(pipe.scheduler.config)

    ## prompt ( 그리고자 하는 이미지 )
    # prompt = "masterpiece, transparent background, realistic, " + "suit, 20s, Korean, student, male,"# + prompt_engineering(prompt_user)
    # prompt = "white background, 4K, really, handsome, suit, 20s, Korean, student, 1male,"# + prompt_engineering(prompt_user)
    prompt = "white background, 4K, really," + prompt_engineering(prompt_user)

    # negative_prompt_user = "nsfw, lowres, bad hands, semi-realistic, anime," + negative_prompt_user
    negative_prompt_user = "(worst quality:2),(low quality:2),(normal quality:2),lowres,watermark,"
    # negative_prompt_user = 'nsfw'
    ## negative_prompt ( 그림에 없었으면 하는 것 )
    negative_prompt = negative_prompt_user

    ## 학습 반복횟수
    num_steps = num_steps_user

    # 스케일 설정
    guidance_scale = guidance_scale_users

    out_image = pipe(
        prompt,
        negative_prompt=negative_prompt,
        num_inference_steps=num_steps,
        guidance_scale=guidance_scale,
        image=pose_image,
    ).images[0]

    out_image.save(f"repository/image/{user_path}/{user_path}.png")
    # out_image.save(f"front_server/endproject/src/main/resources/apiResult/image/{user_path}/{user_path}.png")

    ## 이미지 출력
    return user_path+".png"
