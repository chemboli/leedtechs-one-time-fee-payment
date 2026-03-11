import { TestBed } from '@angular/core/testing';

import { Format } from './format.service';

describe('Format', () => {
  let service: Format;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Format);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
