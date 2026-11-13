
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Metrica {
  id: number;
  servidor: {
    id: number;
    nombre: string;
  };
  usoCpu: number;
  usoMemoria: number;
  usoDisco: number;
  fechaRegistro: string;
}

@Injectable({
  providedIn: 'root'
})
export class MetricaService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/api/metricas';

  listar(): Observable<Metrica[]> {
    return this.http.get<Metrica[]>(this.apiUrl);
  }
}
