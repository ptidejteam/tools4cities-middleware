import json
import requests

if __name__ == "__main__":
    base_url = "http://localhost:8080"
    route = "/apply/async/"
    runnerId = "507a9418-ee44-44d5-8d0e-3f8c74e27443"
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

    res = requests.get(url=base_url + route + runnerId, data=data)
    print(res.text)