/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PacienteDetailComponent from '@/entities/paciente/paciente-details.vue';
import PacienteClass from '@/entities/paciente/paciente-details.component';
import PacienteService from '@/entities/paciente/paciente.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Paciente Management Detail Component', () => {
    let wrapper: Wrapper<PacienteClass>;
    let comp: PacienteClass;
    let pacienteServiceStub: SinonStubbedInstance<PacienteService>;

    beforeEach(() => {
      pacienteServiceStub = sinon.createStubInstance<PacienteService>(PacienteService);

      wrapper = shallowMount<PacienteClass>(PacienteDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { pacienteService: () => pacienteServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPaciente = { id: 123 };
        pacienteServiceStub.find.resolves(foundPaciente);

        // WHEN
        comp.retrievePaciente(123);
        await comp.$nextTick();

        // THEN
        expect(comp.paciente).toBe(foundPaciente);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPaciente = { id: 123 };
        pacienteServiceStub.find.resolves(foundPaciente);

        // WHEN
        comp.beforeRouteEnter({ params: { pacienteId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.paciente).toBe(foundPaciente);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
