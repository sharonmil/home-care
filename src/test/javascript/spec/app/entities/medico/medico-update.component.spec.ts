/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MedicoUpdateComponent from '@/entities/medico/medico-update.vue';
import MedicoClass from '@/entities/medico/medico-update.component';
import MedicoService from '@/entities/medico/medico.service';

import UserService from '@/entities/user/user.service';

import PacienteService from '@/entities/paciente/paciente.service';
import AlertService from '@/shared/alert/alert.service';

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
  describe('Medico Management Update Component', () => {
    let wrapper: Wrapper<MedicoClass>;
    let comp: MedicoClass;
    let medicoServiceStub: SinonStubbedInstance<MedicoService>;

    beforeEach(() => {
      medicoServiceStub = sinon.createStubInstance<MedicoService>(MedicoService);

      wrapper = shallowMount<MedicoClass>(MedicoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          medicoService: () => medicoServiceStub,
          alertService: () => new AlertService(),

          userService: () => new UserService(),

          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
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
        comp.medico = entity;
        medicoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medicoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.medico = entity;
        medicoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medicoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMedico = { id: 123 };
        medicoServiceStub.find.resolves(foundMedico);
        medicoServiceStub.retrieve.resolves([foundMedico]);

        // WHEN
        comp.beforeRouteEnter({ params: { medicoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.medico).toBe(foundMedico);
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
