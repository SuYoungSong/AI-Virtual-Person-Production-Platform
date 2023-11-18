import torch
from transformers import AutoModelForCausalLM, AutoTokenizer
import openai
import json, requests
from bardapi import Bard, BardCookies
import browser_cookie3
import requests

def gen(x, model, tokenizer, device):
    context = '''
            지시사항
            1. 답변은 영어로 해야합니다.
            2. 제공한 내용에서 캐릭터를 형상화 할 수 있는 단어만 추출합니다. 예를 들어 [내용: 빨간색 머리의 후드티를 입은 남학생]은 [답변: red hair, hoodie, student, male]이 되어야 합니다.
            3. 각 단어를 컴마(,) 단위로 구분합니다.
            4. 응답은 반드시 단어와 컴마(,)로만 구성되어야 합니다. 이 외의 말은 답변하면 안됩니다.
            '''
    prompt = (
        f"{context}\n내용: {x}\n답변:"
    )
    len_prompt = len(prompt)
    gened = model.generate(
        **tokenizer(prompt, return_tensors="pt", return_token_type_ids=False).to(
            device
        ),
        max_new_tokens=1024,
        early_stopping=True,
        do_sample=True,
        top_k=20,
        top_p=0.92,
        no_repeat_ngram_size=3,
        eos_token_id=2,
        repetition_penalty=1.2,
        num_beams=3
    )
    return tokenizer.decode(gened[0])[len_prompt:]

def LLM_infer(input):
    device = (
        torch.device("cuda:0") if torch.cuda.is_available() else torch.device("cpu")
    )
    model_id = "kfkas/Llama-2-ko-7b-Chat"
    model = AutoModelForCausalLM.from_pretrained(
        model_id, device_map={"": 0},torch_dtype=torch.float16, low_cpu_mem_usage=True
    )
    tokenizer = AutoTokenizer.from_pretrained(model_id)
    model.eval()
    model.config.use_cache = (True)
    tokenizer.pad_token = tokenizer.eos_token
    output = gen(input, model=model, tokenizer=tokenizer, device=device)

    return output


def prompt_engineering(prompt):
    text = LLM_infer(prompt)
    # text = prompt_engineering_gpt(prompt)
    # text = kogpt_api(prompt)
    text = text.replace("답변",'')
    text = text.replace(":",'')
    text = text.replace("</s>",'')

    print('변경된 text:',text)
    return text

def prompt_engineering_gpt(x):
    openai.api_key ="GPT KEY"

    context = '''
	        지시사항
            1. 답변은 영어로 해야합니다.
            2. 제공한 내용에서 캐릭터를 형상화 할 수 있는 단어만 추출합니다. 예를 들어 [내용: 빨간색 머리의 후드티를 입은 남학생]은 [답변: red hair, hoodie, student, male]이 되어야 합니다.
            3. 각 단어를 컴마(,) 단위로 구분합니다.
            4. 응답은 반드시 단어와 컴마(,)로만 구성되어야 합니다. 이 외의 말은 답변하면 안됩니다.
            '''

    prompt = f"{context}\n내용: {x}\n답변:"


    response = openai.Completion.create(
      model="text-davinci-003",
      prompt=prompt,
      temperature=1,
      max_tokens=20,
      top_p=1,
      frequency_penalty=0,
      presence_penalty=0
    )
    answer = (response.choices[0].text)

    return answer


def kogpt_api(x,max_tokens = 30, temperature = 0.2, top_p = 0.2, n = 1):
    REST_API_KEY = 'KAKAO KOGPT API'
    context = '''
    	        지시사항
                1. 답변은 무조건 영어로 해야합니다.
                2. 제공한 내용에서 캐릭터를 형상화 할 수 있는 단어만 추출합니다. 예를 들어 [내용: 빨간색 머리의 후드티를 입은 남학생]은 [답변: red hair, hoodie, student, male]이 되어야 합니다.
                3. 각 단어를 컴마(,) 단위로 구분합니다.
                4. 응답은 반드시 단어와 컴마(,)로만 구성되어야 합니다. 이 외의 말은 답변하면 안됩니다.
                '''

    prompt = f"{context}\n내용: {x}\n답변:"
    r = requests.post(
        'https://api.kakaobrain.com/v1/inference/kogpt/generation',
        json = {
            'prompt': prompt,
            'max_tokens': max_tokens,
            'temperature': temperature,
            'top_p': top_p,
            'n': n
        },
        headers = {
            'Authorization': 'KakaoAK ' + REST_API_KEY,
            'Content-Type': 'application/json'
        }
    )
    # 응답 JSON 형식으로 변환
    response = json.loads(r.content)
    return response['generations'][0]['text']

def bard_api(x):
    headers = {
        "Accept-Language" : "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6,zh-CN;q=0.5,zh;q=0.4",
    }
    url = 'https://bard.google.com/chat/'
    cj = browser_cookie3.chrome()
    r = requests.get(url, cookies=cj, headers=headers)

    # token = r.cookies.get('__Secure-1PSIDCC')

    # token = 'bwjdYTlb0PQS75WEQOmr5REPyAz3RlX3dmPEq2dDKJmPCw1g-vekI36_-BWUjzmNCauDQQ.'
    # bard = Bard(token=token)

    # cookie_dict = {
    #     "__Secure-1PSID": r.cookies.get('__Secure-1PSID'),
    #     "__Secure-1PSIDTS": r.cookies.get('__Secure-1PSIDTS'),
    #     "__Secure-1PSIDCC": r.cookies.get('__Secure-1PSIDCC'),
    # }
    cookie_dict = {
        "__Secure-1PSID": "COOKIE API KEY",
        "__Secure-1PSIDTS": "COOKIE API KEY",
        "__Secure-1PSIDCC": "COOKIE API KEY",
    }

    bard = BardCookies(cookie_dict=cookie_dict)
    context = '''
    	        지시사항
                1. 답변은 무조건 영어로 해야합니다.
                2. 제공한 내용에서 캐릭터를 형상화 할 수 있는 단어만 추출합니다. 예를 들어 [내용: 빨간색 머리의 후드티를 입은 남학생]은 [답변: red hair, hoodie, student, male]이 되어야 합니다.
                3. 각 단어를 컴마(,) 단위로 구분합니다.
                4. 답변은 반드시 단어와 컴마(,)로만 구성되어야 합니다. 이 외의 말은 답변하면 안됩니다.
                '''

    prompt = f"{context}\n\n내용: {x}\n\n답변:"
    result = bard.get_answer(prompt)['content']
    result = result.replace("```","[답변구역]")
    out_text = result[result.find("[답변구역]"):result.rfind("[답변구역]")]
    out_text = out_text.replace("[답변구역]","")
    out_text = out_text.replace("\n", "")
    return out_text