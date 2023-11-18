import torch
import numpy as np
import os
from torch_hybrid_tacotron2.model.model_inference_v2 import Tacotron2
from torch_hybrid_tacotron2.vocoder.model.waveglow import WaveGlow
from torch_hybrid_tacotron2.vocoder.denoiser_librosa import Denoiser
from torch_hybrid_tacotron2.korean_text.korean_cleaner_cls import KoreanCleaner
from torch_hybrid_tacotron2.text import text_to_sequence
from torch_hybrid_tacotron2.utils.util import to_var
import soundfile as sf
import sys, io


def make_voice(user_path, text, model_name, sample_rate_user=22050, sigma_user=0.666, strength_user=3, is_video_reposi = "0"):
    sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')
    sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding='utf-8')

    # 오디오 저장공간 생성
    # os.makedirs(f"front_server/endproject/src/main/resources/apiResult/audio/{user_path}", exist_ok=True)
    if is_video_reposi == "1":
        os.makedirs(f"repository/video_reposi/{user_path}", exist_ok=True)
    else:
        os.makedirs(f"repository/audio/{user_path}", exist_ok=True)
    device = 'cuda' # cuda

    # Tacotron2
    ckpt_dict = torch.load(f'torch_hybrid_tacotron2/logs/model/{model_name}/acoustic.ckpt', map_location=torch.device(device))
    model = Tacotron2()
    model.load_state_dict(ckpt_dict['model'])
    model = model.eval()

    # Vocoder
    ckpt_dict = torch.load(f'torch_hybrid_tacotron2/logs/model/{model_name}/vocoder.ckpt', map_location=torch.device(device))
    vocoder = WaveGlow()
    vocoder.load_state_dict(ckpt_dict['model'])
    vocoder = vocoder.remove_weightnorm(vocoder)
    vocoder.eval()
    denoiser = Denoiser(vocoder, 0.1)

    korean_cleaner = KoreanCleaner()

    text = korean_cleaner.clean_text(text)


    sequence = text_to_sequence(text, ['multi_cleaner'])
    sequence = to_var(torch.IntTensor(sequence)[None, :]).long()


    # 음질 조정해보면서 확인
    sigma = sigma_user
    # 잡음 제거 정도
    strength = strength_user

    sample_rate = sample_rate_user

    with torch.no_grad():
        _, mel_outputs_postnet, linear_outputs, _, alignments = model.inference(sequence)
        wav = vocoder.infer(mel_outputs_postnet, sigma=sigma)

        wav *= 32767. / max(0.01, torch.max(torch.abs(wav)))
        wav = wav.squeeze()
        wav = wav.cpu().detach().numpy().astype('float32')
        wav = denoiser(wav, strength=strength)

    wav = np.append(wav, np.array([[0.0] * (sample_rate // 2)]))

    wav_file = wav.astype(np.int16)


    # sf.write(f'front_server/endproject/src/main/resources/apiResult/audio/{user_path}/{user_path}.wav', wav_file, sample_rate)
    sf.write(f'repository/audio/{user_path}/{user_path}.wav', wav_file, sample_rate)

    if is_video_reposi == "1":
        sf.write(f'repository/video_reposi/{user_path}/{user_path}.wav', wav_file, sample_rate)
    else:
        sf.write(f'repository/audio/{user_path}/{user_path}.wav', wav_file, sample_rate)

    return user_path+ ".wav"