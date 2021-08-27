from suds.client import Client

SOAP_SERVICE_URL = "http://localhost:7002/ws/EstudianteWebServices?wsdl"


class URL:

    def __init__(self, longUrl, shortUrl, createdAt, user, statistics):
        self.longUrl = longUrl
        self.shortUrl = shortUrl
        self.createdAt = createdAt
        self.user = user
        self.statistics = statistics


def show_menu():
    print("")
    print("1. Acortar URL")
    print("2. Ver URLs de un usuario")

def get_option():
    option = int(input("Digite una opcion: "))
    return option


def short_url(client, url):
    return client.service


def main():
    client = Client(url=SOAP_SERVICE_URL)
    running = True

    while (running):
        show_menu()
        option = get_option()

        if option == 1:
            print()
            url = input("Digite URL a acortar")
            response = client.service.shortUrl(url)
            print()
            print("URL acortada con exito. Mostrando informacion: ")
            print("Usuario: " + response.user)
            print("Fecha de creacion: " + response.createdAt)
            print("URL original: " + response.longUrl)
            print("URL acortada: " + response.shortUrl)

            print("Estadisticas:")
            statistics = response.statistics
            print("Clicks: " + str(statistics.clicks))
            print("Clicks unicos: " + str(statistics.uniqueClicks))
            print("Clicks por pais: " + str(statistics.clicksByCountry))
            print("Clicks en las ultimas 24 horas: " + str(statistics.clicksLast24Hours))
            print("Agrupado por navegador: " + statistics.groupedByBrowser)
            print("Agrupado por sistema operativo: " + statistics.groupedByOs)
            print("Agrupado por plataforma: " + statistics.groupedByPlatform)

        elif option == 2:
            print()
            username = input("Digite el nombre usuario")
            page = int(input("Digite el numero de pagina"))
            response = client.service.getPageByUser(username, page)
            if response is not None:
                print()
                print(response)


main()
