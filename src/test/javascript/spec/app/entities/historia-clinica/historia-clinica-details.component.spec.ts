/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import HistoriaClinicaDetailComponent from '@/entities/historia-clinica/historia-clinica-details.vue';
import HistoriaClinicaClass from '@/entities/historia-clinica/historia-clinica-details.component';
import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';
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
  describe('HistoriaClinica Management Detail Component', () => {
    let wrapper: Wrapper<HistoriaClinicaClass>;
    let comp: HistoriaClinicaClass;
    let historiaClinicaServiceStub: SinonStubbedInstance<HistoriaClinicaService>;

    beforeEach(() => {
      historiaClinicaServiceStub = sinon.createStubInstance<HistoriaClinicaService>(HistoriaClinicaService);

      wrapper = shallowMount<HistoriaClinicaClass>(HistoriaClinicaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { historiaClinicaService: () => historiaClinicaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundHistoriaClinica = { id: 123 };
        historiaClinicaServiceStub.find.resolves(foundHistoriaClinica);

        // WHEN
        comp.retrieveHistoriaClinica(123);
        await comp.$nextTick();

        // THEN
        expect(comp.historiaClinica).toBe(foundHistoriaClinica);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundHistoriaClinica = { id: 123 };
        historiaClinicaServiceStub.find.resolves(foundHistoriaClinica);

        // WHEN
        comp.beforeRouteEnter({ params: { historiaClinicaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.historiaClinica).toBe(foundHistoriaClinica);
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
