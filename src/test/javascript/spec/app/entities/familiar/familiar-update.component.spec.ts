/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FamiliarUpdateComponent from '@/entities/familiar/familiar-update.vue';
import FamiliarClass from '@/entities/familiar/familiar-update.component';
import FamiliarService from '@/entities/familiar/familiar.service';

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
  describe('Familiar Management Update Component', () => {
    let wrapper: Wrapper<FamiliarClass>;
    let comp: FamiliarClass;
    let familiarServiceStub: SinonStubbedInstance<FamiliarService>;

    beforeEach(() => {
      familiarServiceStub = sinon.createStubInstance<FamiliarService>(FamiliarService);

      wrapper = shallowMount<FamiliarClass>(FamiliarUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          familiarService: () => familiarServiceStub,
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
        comp.familiar = entity;
        familiarServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(familiarServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.familiar = entity;
        familiarServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(familiarServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFamiliar = { id: 123 };
        familiarServiceStub.find.resolves(foundFamiliar);
        familiarServiceStub.retrieve.resolves([foundFamiliar]);

        // WHEN
        comp.beforeRouteEnter({ params: { familiarId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.familiar).toBe(foundFamiliar);
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
