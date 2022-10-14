/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import SignosVitalesDetailComponent from '@/entities/signos-vitales/signos-vitales-details.vue';
import SignosVitalesClass from '@/entities/signos-vitales/signos-vitales-details.component';
import SignosVitalesService from '@/entities/signos-vitales/signos-vitales.service';
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
  describe('SignosVitales Management Detail Component', () => {
    let wrapper: Wrapper<SignosVitalesClass>;
    let comp: SignosVitalesClass;
    let signosVitalesServiceStub: SinonStubbedInstance<SignosVitalesService>;

    beforeEach(() => {
      signosVitalesServiceStub = sinon.createStubInstance<SignosVitalesService>(SignosVitalesService);

      wrapper = shallowMount<SignosVitalesClass>(SignosVitalesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { signosVitalesService: () => signosVitalesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundSignosVitales = { id: 123 };
        signosVitalesServiceStub.find.resolves(foundSignosVitales);

        // WHEN
        comp.retrieveSignosVitales(123);
        await comp.$nextTick();

        // THEN
        expect(comp.signosVitales).toBe(foundSignosVitales);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundSignosVitales = { id: 123 };
        signosVitalesServiceStub.find.resolves(foundSignosVitales);

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
