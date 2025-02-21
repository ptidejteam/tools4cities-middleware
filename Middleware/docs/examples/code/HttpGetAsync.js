async function httpGetAsync() {
    const runnerId = "d593c930-7fed-4c7b-ac52-fff946b78c32";
    const requestUrl = `http://localhost:8082/apply/async/${runnerId}`;

    try {
        const response = await fetch(requestUrl);
        console.log("GET Response Code:", response.status);
        const data = await response.text();
        console.log(data);
    } catch (error) {
        console.error("Error:", error);
    }
}

httpGetAsync();