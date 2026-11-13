import psutil

# Obtener uso de CPU
cpu = psutil.cpu_percent(interval=1)

# Obtener uso de memoria RAM
memoria = psutil.virtual_memory().percent

# Obtener uso del disco C:
disco = psutil.disk_usage("C:\\").percent

print("===== MÉTRICAS DE INFRawATCH =====")
print(f"Uso de CPU: {cpu}%")
print(f"Uso de RAM: {memoria}%")
print(f"Uso de disco: {disco}%")