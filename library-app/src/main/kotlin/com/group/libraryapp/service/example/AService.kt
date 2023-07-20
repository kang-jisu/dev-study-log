package com.group.libraryapp.service.example

class AService(
        private val dBService: DBService,
) {
    fun request(req: RequestDto) {
        val data = dBService.get(req.id)

        if ( data == null ) {
            dBService.save(Data(req.id, req.content))
        }
        else {
            dBService.modify(Data(req.id, req.content))
        }
    }
}

data class RequestDto(
        val id: String,
        val content: String,
)