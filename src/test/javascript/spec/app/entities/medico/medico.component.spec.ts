/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MedicoComponent from '@/entities/medico/medico.vue';
import MedicoClass from '@/entities/medico/medico.component';
import MedicoService from '@/entities/medico/medico.service';
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
  describe('Medico Management Component', () => {
    let wrapper: Wrapper<MedicoClass>;
    let comp: MedicoClass;
    let medicoServiceStub: SinonStubbedInstance<MedicoService>;

    beforeEach(() => {
      medicoServiceStub = sinon.createStubInstance<MedicoService>(MedicoService);
      medicoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<MedicoClass>(MedicoComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          medicoService: () => medicoServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      medicoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllMedicos();
      await comp.$nextTick();

      // THEN
      expect(medicoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.medicos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      medicoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(medicoServiceStub.retrieve.callCount).toEqual(1);

      comp.removeMedico();
      await comp.$nextTick();

      // THEN
      expect(medicoServiceStub.delete.called).toBeTruthy();
      expect(medicoServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
