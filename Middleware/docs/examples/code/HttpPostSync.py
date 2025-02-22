import json
import requests

if __name__ == "__main__":
    base_url = "http://localhost:8080"
    route = "/apply/sync"
    data = json.dumps({
        "use": "ca.concordia.encs.citydata.producers.EnergyConsumptionProducer",
        "withParams": [
            {
                "name": "city",
                "value": "montreal"
            }
        ],
        "apply": [
            {
                "name": "ca.concordia.encs.citydata.operations.StringFilterOperation",
                "withParams": [
                    {
                        "name": "filterBy",
                        "value": "09:45:00"
                    }
                ]
            }
        ]
    })


    res = requests.post(url=base_url + route, data=data)
    print(res.text)