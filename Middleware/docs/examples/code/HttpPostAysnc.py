import json
import requests

if __name__ == "__main__":
    base_url = "http://localhost:8080"
    route = "/apply/async"
    data = json.dumps({
        "use": "ca.concordia.encs.citydata.producers.StringProducer",
        "withParams": [
            {
                "name": "generationProcess",
                "value": "gabriel"
            }
        ],
        "apply": [
            {
                "name": "ca.concordia.encs.citydata.operations.StringReplaceOperation",
                "withParams": [
                    {
                        "name": "searchFor",
                        "value": "a"
                    },
                    {
                        "name": "replaceBy",
                        "value": "b"
                    }
                ]
            },
            {
                "name": "ca.concordia.encs.citydata.operations.StringReplaceOperation",
                "withParams": [
                    {
                        "name": "searchFor",
                        "value": "l"
                    },
                    {
                        "name": "replaceBy",
                        "value": "k"
                    }
                ]
            }
        ]
    })
    res = requests.post(url=base_url + route, data=data)
    print(res.text)