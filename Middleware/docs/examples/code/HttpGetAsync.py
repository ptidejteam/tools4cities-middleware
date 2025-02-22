import json
import requests

if __name__ == "__main__":
    base_url = "http://localhost:8082"
    route = "/apply/async/"
    runnerId = "1da07690-e42d-4944-b3fb-030bd2aef7be"

    res = requests.get(url=base_url + route + runnerId)
    print(res.text)