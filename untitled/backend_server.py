
from pose_to_image.pose_to_image import pose_to_image
from SadTalker.make_video import make_video
from torch_hybrid_tacotron2.make_voice import make_voice
from vits.make_voice_vits import make_voice_vits
from RVC1006Nvidia.myinfer_v2_0528 import make_voice_vc
from MVSEP_MDX23_Colab_v2.inference import uvr_start
from kt_tts import kt_tts

from fastapi import FastAPI
from pydantic import BaseModel
from typing import Optional


# text_to_image request body
class Image_info(BaseModel):
  model_version: str
  user_path: str
  prompt_user: str
  negative_prompt_user: str
  model_name: Optional[str] = "majicmixRealistic_v7"
  pose_image_path: Optional[str] = "pose_to_image/pose.png"
  num_step_user: Optional[int] = 20
  guidance_scale_users: Optional[int] = 7

# text_to_speech request body
class Voice_info(BaseModel):
  user_path: str
  text: str
  model_name: str
  sample_rate_user: Optional[int] = 22050
  sigma_user: Optional[int] = 0.666
  strength_user: Optional[int] = 3
  is_video_reposi: Optional[str] = "0"
class Voice_info_Vc(BaseModel):
  f0_key: str
  wav_path: str
  model_name: str

class Voice_info_Kt(BaseModel):
  speaker: str
  prompt: str
  uuid: str
  is_reposi: Optional[str] = "0"
  is_video_reposi: Optional[str] = "0"


class Video_info(BaseModel):
  user_path: str
  image_path: str
  audio_path: str
  is_video_reposi: Optional[str] = "0"

class UVR(BaseModel):
  uuid: str
  cpu: bool = False
  overlap_demucs: float = 0.6
  overlap_MDX: float = 0.5
  overlap_MDXv3: int = 10
  weight_MDXv3: int = 6
  weight_VOCFT: int = 5
  weight_HQ3: int = 2
  single_onnx: bool = False
  chunk_size: int = 1000000
  large_gpu: bool = False
  bigshifts: int = 1
  vocals_only: bool = True
  output_format: str = "FLOAT"

app = FastAPI()



@app.get("/")
def 이름():
  return 'server checking'


@app.post("/api/make_image/")
def make_image(image_info: Image_info):
  return pose_to_image(
    image_info.model_version,
    image_info.user_path,
    image_info.prompt_user,
    image_info.negative_prompt_user,
    image_info.model_name,
    image_info.pose_image_path,
    image_info.num_step_user,
    image_info.guidance_scale_users,
  )

@app.post("/api/make_voice/")
def word_to_voice(voice_info: Voice_info):
  return make_voice(
    voice_info.user_path,
    voice_info.text,
    voice_info.model_name,
    voice_info.sample_rate_user,
    voice_info.sigma_user,
    voice_info.strength_user,
    voice_info.is_video_reposi

  )

@app.post("/api/make_voice_vits/")
def word_to_voice_vits(voice_info: Voice_info):
  return make_voice_vits(
    voice_info.user_path,
    voice_info.text,
    voice_info.model_name,
    voice_info.is_video_reposi
  )

@app.post("/api/make_voice_vc/")
def word_to_voice_vc(voice_info_vc: Voice_info_Vc):
  return make_voice_vc(
    voice_info_vc.f0_key,
    voice_info_vc.wav_path,
    voice_info_vc.model_name,
  )

@app.post("/api/make_voice_kt/")
def word_to_voice_kt(voice_info_kt: Voice_info_Kt):
  return kt_tts(
    voice_info_kt.speaker,
    voice_info_kt.prompt,
    voice_info_kt.uuid,
    voice_info_kt.is_reposi,
    voice_info_kt.is_video_reposi
  )


@app.post("/api/make_video/")
def video_mix(video_info: Video_info):
  return make_video(
    video_info.user_path,
    video_info.image_path,
    video_info.audio_path,
    video_info.is_video_reposi
  )

@app.post("/api/uvr/")
def uvr_wav(uvr : UVR):
  return uvr_start(
    uvr.uuid,
    uvr.cpu,
    uvr.overlap_demucs,
    uvr.overlap_MDX,
    uvr.overlap_MDXv3,
    uvr.weight_MDXv3,
    uvr.weight_VOCFT,
    uvr.weight_HQ3,
    uvr.single_onnx,
    uvr.chunk_size,
    uvr.large_gpu,
    uvr.bigshifts,
    uvr.vocals_only,
    uvr.output_format
  )

#uvicorn backend_server:app --reload