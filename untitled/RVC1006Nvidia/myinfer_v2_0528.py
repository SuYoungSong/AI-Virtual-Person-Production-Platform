'''
v1
runtime\python.exe myinfer-v2-0528.py 0 "E:\codes\py39\RVC-beta\todo-songs\1111.wav" "E:\codes\py39\logs\mi-test\added_IVF677_Flat_nprobe_7.index" harvest "test.wav" "E:\codes\py39\test-20230416b\weights\mi-test.pth" 0.66 cuda:0 True 3 0 1 0.33
v2
runtime\python.exe myinfer-v2-0528.py 0 "E:\codes\py39\RVC-beta\todo-songs\1111.wav" "E:\codes\py39\test-20230416b\logs\mi-test-v2\aadded_IVF677_Flat_nprobe_1_v2.index" harvest "test_v2.wav" "E:\codes\py39\test-20230416b\weights\mi-test-v2.pth" 0.66 cuda:0 True 3 0 1 0.33
'''
import os,sys,pdb,torch

sys.path.append('RVC1006Nvidia/')
now_dir = os.getcwd()
# sys.path.append(now_dir)
import argparse
import glob
import sys
import torch
from multiprocessing import cpu_count
class Config:
    def __init__(self,device,is_half):
        self.device = device
        self.is_half = is_half
        self.n_cpu = 0
        self.gpu_name = None
        self.gpu_mem = None
        self.x_pad, self.x_query, self.x_center, self.x_max = self.device_config()

    def device_config(self) -> tuple:
        if torch.cuda.is_available():
            i_device = int(self.device.split(":")[-1])
            self.gpu_name = torch.cuda.get_device_name(i_device)
            if (
                ("16" in self.gpu_name and "V100" not in self.gpu_name.upper())
                or "P40" in self.gpu_name.upper()
                or "1060" in self.gpu_name
                or "1070" in self.gpu_name
                or "1080" in self.gpu_name
            ):
                print("16系/10系显卡和P40强制单精度")
                self.is_half = False
                for config_file in ["32k.json", "40k.json", "48k.json"]:
                    with open(f"configs/{config_file}", "r") as f:
                        strr = f.read().replace("true", "false")
                    with open(f"configs/{config_file}", "w") as f:
                        f.write(strr)
                with open("trainset_preprocess_pipeline_print.py", "r") as f:
                    strr = f.read().replace("3.7", "3.0")
                with open("trainset_preprocess_pipeline_print.py", "w") as f:
                    f.write(strr)
            else:
                self.gpu_name = None
            self.gpu_mem = int(
                torch.cuda.get_device_properties(i_device).total_memory
                / 1024
                / 1024
                / 1024
                + 0.4
            )
            if self.gpu_mem <= 4:
                with open("trainset_preprocess_pipeline_print.py", "r") as f:
                    strr = f.read().replace("3.7", "3.0")
                with open("trainset_preprocess_pipeline_print.py", "w") as f:
                    f.write(strr)
        elif torch.backends.mps.is_available():
            print("没有发现支持的N卡, 使用MPS进行推理")
            self.device = "mps"
        else:
            print("没有发现支持的N卡, 使用CPU进行推理")
            self.device = "cpu"
            self.is_half = True

        if self.n_cpu == 0:
            self.n_cpu = cpu_count()

        if self.is_half:
            # 6G显存配置
            x_pad = 3
            x_query = 10
            x_center = 60
            x_max = 65
        else:
            # 5G显存配置
            x_pad = 1
            x_query = 6
            x_center = 38
            x_max = 41

        if self.gpu_mem != None and self.gpu_mem <= 4:
            x_pad = 1
            x_query = 5
            x_center = 30
            x_max = 32

        return x_pad, x_query, x_center, x_max

# f0up_key=sys.argv[1]                    # 0
# input_path=sys.argv[2]                  # wav path 변경할 wav
# index_path=sys.argv[3]                  # index path
# f0method=sys.argv[4]                    # harvest or pm or crepe or rmvpe
# opt_path=sys.argv[5]                    # test.wav 저장할 파일 이름 느낌
# model_path=sys.argv[6]                  # pth path
# index_rate=float(sys.argv[7])           # 0.66
# device=sys.argv[8]                      # cuda:0
# is_half=bool(sys.argv[9])               # True
# filter_radius=int(sys.argv[10])         # 3
# resample_sr=int(sys.argv[11])           # 0
# rms_mix_rate=float(sys.argv[12])        # 1
# protect=float(sys.argv[13])             # 0.33
# print(sys.argv)
#
# config=Config(device,is_half)
# now_dir=os.getcwd()
# sys.path.append(now_dir)
from vc_infer_pipeline import VC
from infer.lib.infer_pack.models import (
    SynthesizerTrnMs256NSFsid,
    SynthesizerTrnMs256NSFsid_nono,
    SynthesizerTrnMs768NSFsid,
    SynthesizerTrnMs768NSFsid_nono,
)
from infer.lib.audio import load_audio
from fairseq import checkpoint_utils
from scipy.io import wavfile

hubert_model=None
def load_hubert():
    global hubert_model
    models, saved_cfg, task = checkpoint_utils.load_model_ensemble_and_task(["RVC1006NVidia/assets/hubert/hubert_base.pt"],suffix="",)
    hubert_model = models[0]
    hubert_model = hubert_model.to(device)
    if(is_half):hubert_model = hubert_model.half()
    else:hubert_model = hubert_model.float()
    hubert_model.eval()

def vc_single(sid,input_audio,f0_up_key,f0_file,f0_method,file_index,index_rate):
    global tgt_sr,net_g,vc,hubert_model,version
    if input_audio is None:return "You need to upload an audio", None
    f0_up_key = int(f0_up_key)
    audio=load_audio(input_audio,16000)
    times = [0, 0, 0]
    if(hubert_model==None):load_hubert()
    if_f0 = cpt.get("f0", 1)
    # audio_opt=vc.pipeline(hubert_model,net_g,sid,audio,times,f0_up_key,f0_method,file_index,file_big_npy,index_rate,if_f0,f0_file=f0_file)
    audio_opt=vc.pipeline(hubert_model,net_g,sid,audio,input_audio,times,f0_up_key,f0_method,file_index,index_rate,if_f0,filter_radius,tgt_sr,resample_sr,rms_mix_rate,version,protect,f0_file=f0_file)
    print(times)
    return audio_opt


def get_vc(model_path):
    global n_spk,tgt_sr,net_g,vc,cpt,device,is_half,version
    print("loading pth %s"%model_path)
    cpt = torch.load(model_path, map_location="cpu")
    tgt_sr = cpt["config"][-1]
    cpt["config"][-3]=cpt["weight"]["emb_g.weight"].shape[0]#n_spk
    if_f0=cpt.get("f0",1)
    version = cpt.get("version", "v1")
    if version == "v1":
        if if_f0 == 1:
            net_g = SynthesizerTrnMs256NSFsid(*cpt["config"], is_half=is_half)
        else:
            net_g = SynthesizerTrnMs256NSFsid_nono(*cpt["config"])
    elif version == "v2":
        if if_f0 == 1:#
            net_g = SynthesizerTrnMs768NSFsid(*cpt["config"], is_half=is_half)
        else:
            net_g = SynthesizerTrnMs768NSFsid_nono(*cpt["config"])
    del net_g.enc_q
    print(net_g.load_state_dict(cpt["weight"], strict=False))  # 不加这一行清不干净，真奇葩
    net_g.eval().to(device)
    if (is_half):net_g = net_g.half()
    else:net_g = net_g.float()
    vc = VC(tgt_sr, config)
    n_spk=cpt["config"][-3]
    # return {"visible": True,"maximum": n_spk, "__type__": "update"}

#
# get_vc(model_path)
# wav_opt=vc_single(0,input_path,f0up_key,None,f0method,index_path,index_rate)
# wavfile.write(opt_path, tgt_sr, wav_opt)


f0up_key=None                    # 0
input_path=None                  # wav path 변경할 wav
index_path=None                  # index path
f0method=None                    # harvest or pm or crepe or rmvpe
opt_path=None                    # test.wav 저장할 파일 이름 느낌
model_path=None                  # pth path
index_rate=None           # 0.66
device=None                      # cuda:0
is_half=None               # True
filter_radius=None         # 3
resample_sr=None           # 0
rms_mix_rate=None        # 1
protect=None             # 0.33
config = None

def make_voice_vc(f0_key, wav_path, model_name):
    # 매개변수
    # infer( 음역key, 추론wav파일경로, 모델 이름 )
    global f0up_key
    global input_path
    global index_path
    global f0method
    global opt_path
    global model_path
    global index_rate
    global device
    global is_half
    global filter_radius
    global resample_sr
    global rms_mix_rate
    global protect
    global config

    base_path = "D:/WorkSpace/untitled/RVC1006Nvidia/assets/"

    f0up_key = int(f0_key)              # 음역대 조절 -12 ~ 0 ~ 12
    input_path = wav_path           # 추론할 wav 파일
    index_path = base_path + "indexs/" + model_name + ".index"            # index 경로
    f0method = "pm"              # harvest or pm or crepe or rmvpe
    opt_path = input_path[:input_path.rindex('.')] + ".wav"           # 저장할 파일 경로 및 이름
    model_path = base_path + "weights/" + model_name + ".pth"            # 모델 pth 경로
    index_rate = 0.66               # 0.66   ( 검색 피쳐 비율 , 악센트 잡기 but 높으면 아티팩트 발생 )
    device = "cuda:0"               # cuda:0
    is_half = True                  # True
    filter_radius = 3               # 3 ( 호흡 잡기,  3이상인경우 중앙값 필터링 적용된다. )  ( 0 ~ 7 정수 )
    resample_sr = 0                 # 0 or 변경할 오디오 샘플링 ( 0, 16000, 24000, 22050, 40000, 44100, 48000 )
    rms_mix_rate = 1                # 1  ( 볼륨 포락선 스케일링, 0에 가까울수록 원래 볼륨, 1에 가까울수록 볼륨이 커진다. 소음이 크면 줄이기 ) ( 0.05단위로 조정) ( 0.00~1.00 )
    protect = 0.33                  # 0.33       ( 무성 자음과 숨소리 보호 비활성화시 0.5 입력 ) ( 0.01단위로 조정) ( 0.00~0.05 )

    config = Config(device, is_half)
    now_dir = os.getcwd()
    sys.path.append(now_dir)

    infer_start()

    return "1"

def infer_start():
    get_vc(model_path)
    wav_opt = vc_single(0, input_path, f0up_key, None, f0method, index_path, index_rate)
    wavfile.write(opt_path, tgt_sr, wav_opt)