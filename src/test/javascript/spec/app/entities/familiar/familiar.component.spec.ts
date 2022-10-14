/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FamiliarComponent from '@/entities/familiar/familiar.vue';
import FamiliarClass from '@/entities/familiar/familiar.component';
import FamiliarService from '@/entities/familiar/familiar.service';
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
  describe('Familiar Management Component', () => {
    let wrapper: Wrapper<FamiliarClass>;
    let comp: FamiliarClass;
    let familiarServiceStub: SinonStubbedInstance<FamiliarService>;

    beforeEach(() => {
      familiarServiceStub = sinon.createStubInstance<FamiliarService>(FamiliarService);
      familiarServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<FamiliarClass>(FamiliarComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          familiarService: () => familiarServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      familiarServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllFamiliars();
      await comp.$nextTick();

      // THEN
      expect(familiarServiceStub.retrieve.called).toBeTruthy();
      expect(comp.familiars[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      familiarServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(familiarServiceStub.retrieve.callCount).toEqual(1);

      comp.removeFamiliar();
      await comp.$nextTick();

      // THEN
      expect(familiarServiceStub.delete.called).toBeTruthy();
      expect(familiarServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
