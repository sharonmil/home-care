/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/date/filters';
import FamiliarService from '@/entities/familiar/familiar.service';
import { Familiar } from '@/shared/model/familiar.model';
import { Parentesco } from '@/shared/model/enumerations/parentesco.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Familiar Service', () => {
    let service: FamiliarService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new FamiliarService();
      currentDate = new Date();
      elemDefault = new Familiar(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, Parentesco.PADRE_O_MADRE);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Familiar', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Familiar', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Familiar', async () => {
        const returnedFromService = Object.assign(
          {
            cedula: 'BBBBBB',
            ciudad: 'BBBBBB',
            direccion: 'BBBBBB',
            telefono: 'BBBBBB',
            fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
            parentesco: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Familiar', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Familiar', async () => {
        const patchObject = Object.assign(
          {
            cedula: 'BBBBBB',
            direccion: 'BBBBBB',
            parentesco: 'BBBBBB',
          },
          new Familiar()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Familiar', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Familiar', async () => {
        const returnedFromService = Object.assign(
          {
            cedula: 'BBBBBB',
            ciudad: 'BBBBBB',
            direccion: 'BBBBBB',
            telefono: 'BBBBBB',
            fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
            parentesco: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Familiar', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Familiar', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Familiar', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
