
import {
  Component,
  inject,
  OnInit,
  OnDestroy,
  ChangeDetectorRef
} from '@angular/core';

import { interval, Subscription } from 'rxjs';
import { Metrica, MetricaService } from './services/metrica';

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit, OnDestroy {

  private metricaService = inject(MetricaService);

  private cdr = inject(ChangeDetectorRef);

  ultimaMetrica: Metrica | null = null;

  error = '';

  private actualizacion?: Subscription;

  ngOnInit(): void {
    console.log('InfraWatch iniciado');

    this.cargarMetricas();

    this.actualizacion = interval(30000).subscribe(() => {
      console.log('Actualizando métricas automáticamente...');
      this.cargarMetricas();
    });
  }

  cargarMetricas(): void {
    this.metricaService.listar().subscribe({
      next: (metricas) => {
        const metricasWindows = metricas
          .filter(metrica => metrica.servidor.id === 2)
          .sort((a, b) =>
            new Date(b.fechaRegistro).getTime() -
            new Date(a.fechaRegistro).getTime()
          );

        this.ultimaMetrica = metricasWindows[0] ?? null;

        this.error = '';

        console.log('Última métrica recibida:', this.ultimaMetrica);

        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error('Error al obtener métricas:', err);

        this.error = 'No se pudieron cargar las métricas.';

        this.cdr.detectChanges();
      }
    });
  }

  ngOnDestroy(): void {
    this.actualizacion?.unsubscribe();
  }
}
