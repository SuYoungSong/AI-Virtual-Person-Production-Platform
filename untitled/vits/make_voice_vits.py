import sys

sys.path.append('vits/')

import IPython.display as ipd
import torch
import commons
import utils
from models import SynthesizerTrn
from text.symbols import symbols
from text import text_to_sequence
from scipy.io.wavfile import write
import os

def make_voice_vits(user_path, text, model_name, is_video_reposi = "0"):
    # 오디오 저장공간 생성

    if is_video_reposi == "1":
        os.makedirs(f"repository/video_reposi/{user_path}", exist_ok=True)
    else:
        os.makedirs(f"repository/audio/{user_path}", exist_ok=True)

    # os.makedirs(f"front_server/endproject/src/main/resources/apiResult/audio/{user_path}", exist_ok=True)

    class vits():
        def __init__(self, checkpoint_path, config_path):
            self.hps = utils.get_hparams_from_file(config_path)
            self.spk_count = self.hps.data.n_speakers
            self.net_g = SynthesizerTrn(
                len(symbols),
                self.hps.data.filter_length // 2 + 1,
                self.hps.train.segment_size // self.hps.data.hop_length,
                n_speakers=self.hps.data.n_speakers,
                **self.hps.model).cuda()
            _ = self.net_g.eval()
            _ = utils.load_checkpoint(checkpoint_path, self.net_g, None)

        def get_text(self, text, hps):
            text_norm = text_to_sequence(text, hps.data.text_cleaners)
            if hps.data.add_blank:
                text_norm = commons.intersperse(text_norm, 0)
            text_norm = torch.LongTensor(text_norm)
            return text_norm

        def infer(self, text,save_file_name="infer/test.wav", spk_id=0):
            ipd.clear_output()
            stn_tst = self.get_text(text, self.hps)
            with torch.no_grad():
                x_tst = stn_tst.cuda().unsqueeze(0)
                x_tst_lengths = torch.LongTensor([stn_tst.size(0)]).cuda()
                sid = torch.LongTensor([spk_id]).cuda()
                audio = self.net_g.infer(x_tst, x_tst_lengths, sid=sid, noise_scale=.667, noise_scale_w=0.8, length_scale=1)[0][0,0].data.cpu().float().numpy()
            write(save_file_name, self.hps.data.sampling_rate, audio)
            ipd.display(ipd.Audio(audio, rate=self.hps.data.sampling_rate, normalize=False))


    tts = vits(f'vits/ttS_model/{model_name}.pth', f'vits/ttS_model/{model_name}.json')

    if is_video_reposi == "1":
        tts.infer(text, f'repository/video_reposi/{user_path}/{user_path}.wav', 0)
    else:
        tts.infer(text, f'repository/audio/{user_path}/{user_path}.wav', 0)


    # tts.infer(text, f'front_server/endproject/src/main/resources/apiResult/audio/{user_path}/{user_path}.wav', 0)

    return user_path+".wav"