
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Alerta {
  id: number;
  servidor: {
    id: number;
    nombre: string;
  };
  recurso: string;
  nivel: string;
  valor: number;
  mensaje: string;
  activa: boolean;
  fechaCreacion: string;
  fechaResolucion: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class AlertaService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/api/alertas';

  listar(): Observable<Alerta[]> {
    return this.http.get<Alerta[]>(this.apiUrl);
  }

  listarActivas(): Observable<Alerta[]> {
    return this.http.get<Alerta[]>(
      `${this.apiUrl}/activas`
    );
  }

  listarPorServidor(servidorId: number): Observable<Alerta[]> {
    return this.http.get<Alerta[]>(
      `${this.apiUrl}/servidor/${servidorId}`
    );
  }
}
