async function httpPostAsync() {
    const requestUrl = "http://localhost:8082/apply/async";  // Replace with actual URL
    const jsonInput = JSON.stringify(	{
	  "use": "ca.concordia.encs.citydata.producers.RandomNumberProducer",
	  "withParams": [
	    {
	      "name": "listSize",
	      "value": 10
	    }
	  ],
	  "apply": [
	    {
	      "name": "ca.concordia.encs.citydata.operations.AverageOperation",
	      "withParams": [
	        {
	          "name": "roundingMethod",
	          "value": "floor"
	        }
	      ]
	    },
	    {
	      "name": "ca.concordia.encs.citydata.operations.MergeOperation",
	      "withParams": [
	        {
	          "name": "targetProducer",
	          "value": "ca.concordia.encs.citydata.producers.RandomNumberProducer"
	        },
	        {
	          "name": "targetProducerParams",
	          "value": [
	            {
	              "name": "listSize",
	              "value": 4
	            }
	          ]
	        }
	      ]
	    }
	  ]
	});  // Example JSON payload

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

httpPostAsync();