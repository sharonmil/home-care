/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SignosVitalesUpdateComponent from '@/entities/signos-vitales/signos-vitales-update.vue';
import SignosVitalesClass from '@/entities/signos-vitales/signos-vitales-update.component';
import SignosVitalesService from '@/entities/signos-vitales/signos-vitales.service';

import PacienteService from '@/entities/paciente/paciente.service';

import UserService from '@/entities/user/user.service';
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
  describe('SignosVitales Management Update Component', () => {
    let wrapper: Wrapper<SignosVitalesClass>;
    let comp: SignosVitalesClass;
    let signosVitalesServiceStub: SinonStubbedInstance<SignosVitalesService>;

    beforeEach(() => {
      signosVitalesServiceStub = sinon.createStubInstance<SignosVitalesService>(SignosVitalesService);

      wrapper = shallowMount<SignosVitalesClass>(SignosVitalesUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          signosVitalesService: () => signosVitalesServiceStub,
          alertService: () => new AlertService(),

          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          userService: () => new UserService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.signosVitales = entity;
        signosVitalesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(signosVitalesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.signosVitales = entity;
        signosVitalesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(signosVitalesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSignosVitales = { id: 123 };
        signosVitalesServiceStub.find.resolves(foundSignosVitales);
        signosVitalesServiceStub.retrieve.resolves([foundSignosVitales]);

        // WHEN
        comp.beforeRouteEnter({ params: { signosVitalesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.signosVitales).toBe(foundSignosVitales);
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
