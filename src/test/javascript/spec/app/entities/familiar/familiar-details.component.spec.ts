/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FamiliarDetailComponent from '@/entities/familiar/familiar-details.vue';
import FamiliarClass from '@/entities/familiar/familiar-details.component';
import FamiliarService from '@/entities/familiar/familiar.service';
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
  describe('Familiar Management Detail Component', () => {
    let wrapper: Wrapper<FamiliarClass>;
    let comp: FamiliarClass;
    let familiarServiceStub: SinonStubbedInstance<FamiliarService>;

    beforeEach(() => {
      familiarServiceStub = sinon.createStubInstance<FamiliarService>(FamiliarService);

      wrapper = shallowMount<FamiliarClass>(FamiliarDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { familiarService: () => familiarServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFamiliar = { id: 123 };
        familiarServiceStub.find.resolves(foundFamiliar);

        // WHEN
        comp.retrieveFamiliar(123);
        await comp.$nextTick();

        // THEN
        expect(comp.familiar).toBe(foundFamiliar);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFamiliar = { id: 123 };
        familiarServiceStub.find.resolves(foundFamiliar);

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
