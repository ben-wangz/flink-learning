const jsonServer = require('json-server')
const server = jsonServer.create()
const router = jsonServer.router('json_server/router.json')
const middlewares = jsonServer.defaults()

server.use(middlewares)
server.get('/greeting', (req, res) => {
    res.jsonp(req.query["username"])
})
server.use(router)
server.listen(3000, () => {
    console.log('JSON Server is running')
})