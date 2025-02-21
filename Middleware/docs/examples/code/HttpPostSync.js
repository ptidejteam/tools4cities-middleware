async function httpPostSync() {
    const requestUrl = "http://localhost:8082/apply/sync";  // Replace with actual URL
    const jsonInput = JSON.stringify(	{
	  "use": "ca.concordia.encs.citydata.producers.EnergyConsumptionProducer",
	  "withParams": [
	    {
	      "name": "city",
	      "value": "montreal"
	    }
	  ],
	  "apply": [
	    {
	      "name": "ca.concordia.encs.citydata.operations.MergeOperation",
	      "withParams": [
	        {
	          "name": "targetProducer",
	          "value": "ca.concordia.encs.citydata.producers.GeometryProducer"
	        },
	        {
	          "name": "targetProducerParams",
	          "value": [
	            {
	              "name": "city",
	              "value": "montreal"
	            }
	          ]
	        }
	      ]
	    }
	  ]
	});

    try {
        const response = await fetch(requestUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: jsonInput
        });

        console.log("POST Response Code:", response.status);
        const data = await response.text();
        console.log(data);
    } catch (error) {
        console.error("Error:", error);
    }
}

httpPostSync();