## Web-Crawler

Web-crawler is web server with http endpoint, allows receive sites titles by sended urls

### How it use

- Download jar from https://github.com/ziborgiy/web_crawler/releases. Run application with:
```shell script
java -jar web-crawler-assembly-0.1.0-SNAPSHOT.jar
```
Application starts on 8081 port

- Create json example:

```json
{
 	"urls": ["https://my.serverspace.ru/1c/employees/list?pid=119544", "https://stepik.org/users/41664361/courses", "https://music.yandex.ru/home", "https://twitter.com/home", "https://blog.colinbreck.com/kubernetes-liveness-and-readiness-probes-how-to-avoid-shooting-yourself-in-the-foot/", "abcder", "https://blog.oyanglul.us/scala/dotty/en/dependent-types"]
 }
```
- Create and send POST request using http client (i.e. Postman, curl, etc)

- The response should be like:
```json
{
   "https://music.yandex.ru/home": "Яндекс.Музыка — собираем музыку и подкасты для вас",
   "https://blog.colinbreck.com/kubernetes-liveness-and-readiness-probes-how-to-avoid-shooting-yourself-in-the-foot/": "Kubernetes Liveness and Readiness Probes: How to Avoid Shooting Yourself in the Foot",
   "https://twitter.com/home": "",
   "abcder": "Malformed URL: abcder",
   "https://my.serverspace.ru/1c/employees/list?pid=119544": "serverspace.ru - Вход в панель управления",
   "https://stepik.org/users/41664361/courses": "Stepik",
   "https://blog.oyanglul.us/scala/dotty/en/dependent-types": "Dependent Types in Scala 3"
 }
```

If url is incorrect we will look error in value, ie ("abcder": "Malformed URL: abcder")

If title not found we will look empty string instead result, ie ("https://twitter.com/home": "")

### Tasklist

- [x] Create base functionality getting titles from site by url 
- [x] Create test specification for  routes
- [X] Create test specification for type class
- [X] Create test specification for server
- [ ] Append testcases
- [ ] Append documents for new methods
- [ ] Modified tests with tapir
- [X] Resolve problem with too long answer if urls field too big (synchronous requests)
- [ ] Filter unmatched urls before getTitle
- [ ] Add comments
