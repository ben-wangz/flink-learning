const jsonServer = require("json-server")
const server = jsonServer.create()
const router = jsonServer.router("json_server/router.json")
const middlewares = jsonServer.defaults()

server.use(middlewares)
server.get("/api/greeting", (req, res) => {
    res.send(req.query["username"])
})
server.get("/api/data_keeper/queryDataWithPrefix?namePrefix=MeteorologicalDataStream_", (req, res) => {
    res.send({})
})
server.use(router)
server.listen(3000, () => {
    console.log("JSON Server is running")
})