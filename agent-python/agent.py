
import time
import psutil
import requests

# Configuración de InfraWatch
API_URL = "http://localhost:8080/api/metricas"
SERVIDOR_ID = 2
INTERVALO = 30


def obtener_metricas():
    """Obtiene las métricas reales del computador."""

    cpu = psutil.cpu_percent(interval=1)
    memoria = psutil.virtual_memory().percent
    disco = psutil.disk_usage("C:\\").percent

    return {
        "servidor": {
            "id": SERVIDOR_ID
        },
        "usoCpu": cpu,
        "usoMemoria": memoria,
        "usoDisco": disco
    }


def enviar_metricas():
    """Envía las métricas al backend de InfraWatch."""

    metricas = obtener_metricas()

    print("\n===== INFRaWATCH AGENT =====")
    print(f"CPU: {metricas['usoCpu']}%")
    print(f"RAM: {metricas['usoMemoria']}%")
    print(f"Disco: {metricas['usoDisco']}%")

    try:
        respuesta = requests.post(
            API_URL,
            json=metricas,
            timeout=10
        )

        if respuesta.ok:
            print("Estado: Métricas enviadas correctamente.")
            print(f"Código HTTP: {respuesta.status_code}")
        else:
            print("Error al enviar las métricas.")
            print(f"Código HTTP: {respuesta.status_code}")
            print(respuesta.text)

    except requests.exceptions.RequestException as error:
        print("No se pudo conectar con el backend.")
        print(f"Detalle: {error}")


if __name__ == "__main__":

    print("Iniciando agente InfraWatch...")
    print(f"Servidor asociado: {SERVIDOR_ID}")
    print(f"Intervalo: {INTERVALO} segundos")

    try:
        while True:
            enviar_metricas()
            print(f"\nPróxima medición en {INTERVALO} segundos...")
            time.sleep(INTERVALO)

    except KeyboardInterrupt:
        print("\nAgente InfraWatch detenido.")
