/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import MedicoDetailComponent from '@/entities/medico/medico-details.vue';
import MedicoClass from '@/entities/medico/medico-details.component';
import MedicoService from '@/entities/medico/medico.service';
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
  describe('Medico Management Detail Component', () => {
    let wrapper: Wrapper<MedicoClass>;
    let comp: MedicoClass;
    let medicoServiceStub: SinonStubbedInstance<MedicoService>;

    beforeEach(() => {
      medicoServiceStub = sinon.createStubInstance<MedicoService>(MedicoService);

      wrapper = shallowMount<MedicoClass>(MedicoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { medicoService: () => medicoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMedico = { id: 123 };
        medicoServiceStub.find.resolves(foundMedico);

        // WHEN
        comp.retrieveMedico(123);
        await comp.$nextTick();

        // THEN
        expect(comp.medico).toBe(foundMedico);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMedico = { id: 123 };
        medicoServiceStub.find.resolves(foundMedico);

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
