class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: "servers", action: "index")
        "/aui"(view: "/auiindex")
        "500"(view: '/error')
    }
}
