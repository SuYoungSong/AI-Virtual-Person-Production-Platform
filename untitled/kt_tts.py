from datetime import datetime
import hmac, hashlib
from pytz import timezone
import json, requests
import os

def kt_tts(speaker, prompt, uuid, is_reposi, is_video_reposi = "0"):
    timestamp = datetime.now(timezone("Asia/Seoul")).strftime("%Y%m%d%H%M%S%f")[:-3]
    client_key = "GENIE LABS KEY"
    client_id = "GENIE LABS KEY"
    client_secret = "GENIE LABS KEY"

    # HMAC 기반 signature 생성
    signature = hmac.new(
        key=client_secret.encode("UTF-8"), msg=f"{client_id}:{timestamp}".encode("UTF-8"), digestmod=hashlib.sha256
    ).hexdigest()

    url = "https://aiapi.genielabs.ai/kt/voice/tts-getTTS"
    headers = {
         "x-client-key":f"{client_key}",
         "x-client-signature":f"{signature}",
         "x-auth-timestamp": f"{timestamp}",
         "Content-Type": "application/json",
         "charset": "utf-8",
     }
    body = json.dumps({
        "language":"ko",
        "speaker": int(speaker),
        "volume":100,
        "pitch":100,
        "speed":100,
        "text": prompt
    })
    response = requests.post(url, data=body, headers=headers, verify=False)

    if is_reposi == "1":
        filePath = 'D://WorkSpace/untitled/repository/audio_reposi/' + uuid + '/' + uuid + '.wav'
        os.makedirs(f"repository/audio_reposi/{uuid}", exist_ok=True)
    elif is_video_reposi == "1":
        filePath = 'D://WorkSpace/untitled/repository/video_reposi/' + uuid + '/' + uuid + '.wav'
        os.makedirs(f"repository/video_reposi/{uuid}", exist_ok=True)
    else:
        filePath = 'D://WorkSpace/untitled/repository/audio/' + uuid + '/' + uuid + '.wav'
        os.makedirs(f"repository/audio/{uuid}", exist_ok=True)

    with open(filePath, "wb") as f:
        f.write(response.content)

    return uuid+'.wav'