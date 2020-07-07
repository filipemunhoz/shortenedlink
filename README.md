# shortenedlink


sudo docker run -p 6379:6379 redis
run application
curl -X POST http://localhost:8080/link -H "Content-Type: application/json" -d '{"link" : "https://www.bing.com"}'
