class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        // DEFAULT
		"/"                                 (controller:"default")

		"500"(view:'/error')
	}
}
