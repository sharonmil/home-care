/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PacienteComponent from '@/entities/paciente/paciente.vue';
import PacienteClass from '@/entities/paciente/paciente.component';
import PacienteService from '@/entities/paciente/paciente.service';
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
  describe('Paciente Management Component', () => {
    let wrapper: Wrapper<PacienteClass>;
    let comp: PacienteClass;
    let pacienteServiceStub: SinonStubbedInstance<PacienteService>;

    beforeEach(() => {
      pacienteServiceStub = sinon.createStubInstance<PacienteService>(PacienteService);
      pacienteServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PacienteClass>(PacienteComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          pacienteService: () => pacienteServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      pacienteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllPacientes();
      await comp.$nextTick();

      // THEN
      expect(pacienteServiceStub.retrieve.called).toBeTruthy();
      expect(comp.pacientes[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      pacienteServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(pacienteServiceStub.retrieve.callCount).toEqual(1);

      comp.removePaciente();
      await comp.$nextTick();

      // THEN
      expect(pacienteServiceStub.delete.called).toBeTruthy();
      expect(pacienteServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
