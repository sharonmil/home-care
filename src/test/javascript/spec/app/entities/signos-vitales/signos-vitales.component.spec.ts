/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import SignosVitalesComponent from '@/entities/signos-vitales/signos-vitales.vue';
import SignosVitalesClass from '@/entities/signos-vitales/signos-vitales.component';
import SignosVitalesService from '@/entities/signos-vitales/signos-vitales.service';
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
  describe('SignosVitales Management Component', () => {
    let wrapper: Wrapper<SignosVitalesClass>;
    let comp: SignosVitalesClass;
    let signosVitalesServiceStub: SinonStubbedInstance<SignosVitalesService>;

    beforeEach(() => {
      signosVitalesServiceStub = sinon.createStubInstance<SignosVitalesService>(SignosVitalesService);
      signosVitalesServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<SignosVitalesClass>(SignosVitalesComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          signosVitalesService: () => signosVitalesServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      signosVitalesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllSignosVitaless();
      await comp.$nextTick();

      // THEN
      expect(signosVitalesServiceStub.retrieve.called).toBeTruthy();
      expect(comp.signosVitales[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      signosVitalesServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(signosVitalesServiceStub.retrieve.callCount).toEqual(1);

      comp.removeSignosVitales();
      await comp.$nextTick();

      // THEN
      expect(signosVitalesServiceStub.delete.called).toBeTruthy();
      expect(signosVitalesServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
