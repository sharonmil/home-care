/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PacienteUpdateComponent from '@/entities/paciente/paciente-update.vue';
import PacienteClass from '@/entities/paciente/paciente-update.component';
import PacienteService from '@/entities/paciente/paciente.service';

import UserService from '@/entities/user/user.service';

import FamiliarService from '@/entities/familiar/familiar.service';

import MedicoService from '@/entities/medico/medico.service';

import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';

import SignosVitalesService from '@/entities/signos-vitales/signos-vitales.service';
import AlertService from '@/shared/alert/alert.service';
import UserManagementService from "@/admin/user-management/user-management.service";

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Paciente Management Update Component', () => {
    let wrapper: Wrapper<PacienteClass>;
    let comp: PacienteClass;
    let pacienteServiceStub: SinonStubbedInstance<PacienteService>;

    beforeEach(() => {
      pacienteServiceStub = sinon.createStubInstance<PacienteService>(PacienteService);

      wrapper = shallowMount<PacienteClass>(PacienteUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          pacienteService: () => pacienteServiceStub,
          alertService: () => new AlertService(),

          userService: () => new UserService(),

          userManagementService: () => new UserManagementService(),

          familiarService: () =>
            sinon.createStubInstance<FamiliarService>(FamiliarService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          medicoService: () =>
            sinon.createStubInstance<MedicoService>(MedicoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          historiaClinicaService: () =>
            sinon.createStubInstance<HistoriaClinicaService>(HistoriaClinicaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          signosVitalesService: () =>
            sinon.createStubInstance<SignosVitalesService>(SignosVitalesService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.paciente = entity;
        pacienteServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.paciente = entity;
        pacienteServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPaciente = { id: 123 };
        pacienteServiceStub.find.resolves(foundPaciente);
        pacienteServiceStub.retrieve.resolves([foundPaciente]);

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
