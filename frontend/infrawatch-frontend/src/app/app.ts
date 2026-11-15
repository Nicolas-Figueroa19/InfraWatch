
import {
  Component,
  inject,
  OnInit,
  OnDestroy,
  ChangeDetectorRef
} from '@angular/core';

import { DatePipe } from '@angular/common';
import { interval, Subscription } from 'rxjs';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';
import { Metrica, MetricaService } from './services/metrica';
import { Alerta, AlertaService } from './services/alerta';

@Component({
  selector: 'app-root',
  imports: [BaseChartDirective, DatePipe],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit, OnDestroy {

  private metricaService = inject(MetricaService);
  private alertaService = inject(AlertaService);
  private cdr = inject(ChangeDetectorRef);

  ultimaMetrica: Metrica | null = null;

  alertas: Alerta[] = [];
  alertasActivas: Alerta[] = [];
  historialAlertas: Alerta[] = [];

  estadoServidor = 'Sin datos';

  error = '';
  errorAlertas = '';

  private actualizacion?: Subscription;

  opcionesGrafico: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    animation: false,
    scales: {
      y: {
        min: 0,
        max: 100,
        title: {
          display: true,
          text: 'Porcentaje (%)'
        }
      }
    },
    plugins: {
      legend: {
        display: false
      }
    }
  };

  graficoCpu: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      label: 'CPU',
      borderColor: '#2563eb',
      backgroundColor: 'rgba(37, 99, 235, 0.1)',
      tension: 0.3,
      fill: true
    }]
  };

  graficoRam: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      label: 'Memoria RAM',
      borderColor: '#16a34a',
      backgroundColor: 'rgba(22, 163, 74, 0.1)',
      tension: 0.3,
      fill: true
    }]
  };

  graficoDisco: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      label: 'Disco',
      borderColor: '#ea580c',
      backgroundColor: 'rgba(234, 88, 12, 0.1)',
      tension: 0.3,
      fill: true
    }]
  };

  ngOnInit(): void {
    this.cargarMetricas();
    this.cargarAlertas();

    this.actualizacion = interval(30000).subscribe(() => {
      this.actualizarEstadoServidor();
      this.cargarMetricas();
      this.cargarAlertas();
    });
  }

  cargarMetricas(): void {
    this.metricaService.listarPorServidor(2).subscribe({
      next: (metricas) => {
        const ordenadas = [...metricas].sort(
          (a, b) =>
            new Date(b.fechaRegistro).getTime() -
            new Date(a.fechaRegistro).getTime()
        );

        this.ultimaMetrica = ordenadas[0] ?? null;

        this.actualizarEstadoServidor();

        const historial = ordenadas.slice(0, 50).reverse();

        const etiquetas = historial.map(metrica =>
          new Date(metrica.fechaRegistro).toLocaleTimeString('es-CL')
        );

        this.graficoCpu = {
          labels: etiquetas,
          datasets: [{
            ...this.graficoCpu.datasets[0],
            data: historial.map(metrica => metrica.usoCpu)
          }]
        };

        this.graficoRam = {
          labels: etiquetas,
          datasets: [{
            ...this.graficoRam.datasets[0],
            data: historial.map(metrica => metrica.usoMemoria)
          }]
        };

        this.graficoDisco = {
          labels: etiquetas,
          datasets: [{
            ...this.graficoDisco.datasets[0],
            data: historial.map(metrica => metrica.usoDisco)
          }]
        };

        this.error = '';

        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error('Error al obtener métricas:', err);

        this.error = 'No se pudieron cargar las métricas.';

        this.estadoServidor = 'Sin conexión';

        this.cdr.detectChanges();
      }
    });
  }

  cargarAlertas(): void {
    this.alertaService.listarPorServidor(2).subscribe({
      next: (alertas) => {
        this.alertas = [...alertas].sort(
          (a, b) =>
            new Date(b.fechaCreacion).getTime() -
            new Date(a.fechaCreacion).getTime()
        );

        this.alertasActivas = this.alertas.filter(
          alerta => alerta.activa
        );

        this.historialAlertas = this.alertas.filter(
          alerta => !alerta.activa
        );

        this.errorAlertas = '';

        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error('Error al obtener alertas:', err);

        this.errorAlertas = 'No se pudieron cargar las alertas.';

        this.cdr.detectChanges();
      }
    });
  }

  actualizarEstadoServidor(): void {
    if (!this.ultimaMetrica) {
      this.estadoServidor = 'Sin datos';
      return;
    }

    const fechaUltimaMetrica = new Date(
      this.ultimaMetrica.fechaRegistro
    ).getTime();

    const diferencia = Date.now() - fechaUltimaMetrica;

    if (!Number.isFinite(fechaUltimaMetrica)) {
      this.estadoServidor = 'Sin datos';
    } else if (diferencia >= 0 && diferencia <= 90000) {
      this.estadoServidor = 'Conectado';
    } else {
      this.estadoServidor = 'Sin conexión';
    }
  }

  ngOnDestroy(): void {
    this.actualizacion?.unsubscribe();
  }
}
