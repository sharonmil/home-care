import { Module } from 'vuex';

export const translationStore: Module<any, any> = {
  state: {
    currentLanguage: localStorage.getItem('currentLanguage') || 'es',
    languages: {
      es: { name: 'Español' },
    },
  },
  getters: {
    currentLanguage: state => state.currentLanguage,
    languages: state => state.languages,
  },
  mutations: {
    currentLanguage(state, newLanguage) {
      state.currentLanguage = newLanguage;
      localStorage.setItem('currentLanguage', newLanguage);
    },
  },
};
