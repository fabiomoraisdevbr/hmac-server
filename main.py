import time
import hmac
import hashlib
import requests

# constantes
TEMPERATURE = "25.3"
HUMIDITY = "68.0"
PRESSURE = "1013.2"

URL = "http://localhost:8080/message"

SECRET = "A3gjwTYXsuQ4EBLWzGfazFQ7bL4gGfg6VdubsuGsWgMwUAqkcbhP6kf8VBRHLPfyLx5aRyPuTGWM5LL2dx8oH6pvB29XrJobVafE"
ALGORITHM = "sha512"



def create_content(temp: str, humi: str, pres: str, timestamp: int) -> str:
    return ":".join([temp, humi, pres, str(timestamp)])


def sign(payload: str, secret: str) -> str:
    key = secret.encode("utf-8")
    message = payload.encode("utf-8")

    return hmac.new(key, message, hashlib.sha512).hexdigest()




def main():
    timestamp = int(time.time() * 1000)

    content = create_content(
        TEMPERATURE,
        HUMIDITY,
        PRESSURE,
        timestamp
    )

    signature = sign(content, SECRET)

    body = {
        "message": {
            "temperature": TEMPERATURE,
            "humidity": HUMIDITY,
            "pressure": PRESSURE
        },
        "timestamp": timestamp,
        "hash": signature
    }

    response = requests.post(URL, json=body)

    print("Status:", response.status_code)
    print("Response:", response.text)


if __name__ == "__main__":
    main()