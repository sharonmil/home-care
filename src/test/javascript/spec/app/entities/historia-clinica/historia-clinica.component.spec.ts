/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import HistoriaClinicaComponent from '@/entities/historia-clinica/historia-clinica.vue';
import HistoriaClinicaClass from '@/entities/historia-clinica/historia-clinica.component';
import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('HistoriaClinica Management Component', () => {
    let wrapper: Wrapper<HistoriaClinicaClass>;
    let comp: HistoriaClinicaClass;
    let historiaClinicaServiceStub: SinonStubbedInstance<HistoriaClinicaService>;

    beforeEach(() => {
      historiaClinicaServiceStub = sinon.createStubInstance<HistoriaClinicaService>(HistoriaClinicaService);
      historiaClinicaServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<HistoriaClinicaClass>(HistoriaClinicaComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          historiaClinicaService: () => historiaClinicaServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      historiaClinicaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllHistoriaClinicas();
      await comp.$nextTick();

      // THEN
      expect(historiaClinicaServiceStub.retrieve.called).toBeTruthy();
      expect(comp.historiaClinicas[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      historiaClinicaServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(historiaClinicaServiceStub.retrieve.callCount).toEqual(1);

      comp.removeHistoriaClinica();
      await comp.$nextTick();

      // THEN
      expect(historiaClinicaServiceStub.delete.called).toBeTruthy();
      expect(historiaClinicaServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
