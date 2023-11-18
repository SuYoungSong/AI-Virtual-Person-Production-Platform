import subprocess
import os

def make_video(user_path, image_path, audio_path, is_video_reposi = "0"):
    # 비디오 저장공간 생성
    # os.makedirs(f"front_server/endproject/src/main/resources/apiResult/video/{user_path}", exist_ok=True)

    if is_video_reposi == "1":
        os.makedirs(f"repository/video_reposi/{user_path}", exist_ok=True)

        cmd = f'python SadTalker/inference.py ' \
              f'--driven_audio {audio_path} ' \
              f'--source_image {image_path} ' \
              f'--result_dir repository/video_reposi/{user_path} ' \
              f'--still ' \
              f'--preprocess full ' \
              f'--enhancer gfpgan ' \
              f'--expression_scale 1.4 ' \
              f'--background_enhancer realesrgan'
    else:
        os.makedirs(f"repository/video/{user_path}", exist_ok=True)

        cmd = f'python SadTalker/inference.py ' \
              f'--driven_audio repository/audio/{user_path}/{audio_path} ' \
              f'--source_image repository/image/{user_path}/{image_path} ' \
              f'--result_dir repository/video/{user_path} ' \
              f'--still ' \
              f'--preprocess full ' \
              f'--enhancer gfpgan ' \
              f'--expression_scale 1.4 ' \
              f'--background_enhancer realesrgan'



    # f'--input_yaw 20 30 20 ' \
    # print(cmd)

    # result = subprocess.run(cmd, shell=True, capture_output=True, text=True)

    # 실행 결과 출력
    # print(result.stdout)

    process = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, encoding='utf-8')
    while True:
        output = process.stdout.readline()
        if output == '' and process.poll() is not None:
            break
        if output:
            print(output.strip())

    file_name = image_path[0:image_path.rfind('.')]
    if is_video_reposi == "1":
        return f"{user_path}_enhanced.mp4"
    else:
        return f"{file_name}{file_name}_enhanced.mp4"