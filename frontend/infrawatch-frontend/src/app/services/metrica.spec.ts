import { TestBed } from '@angular/core/testing';
import { Metrica } from './metrica';

describe('Metrica', () => {
  let service: Metrica;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Metrica);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
