import { Component, Inject, Vue } from 'vue-property-decorator';
import AccountService from "@/account/account.service";
@Component
export default class EntitiesMenu extends Vue {

  @Inject('accountService') private accountService: () => AccountService;

  private hasAnyAuthorityValues = {};

  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
    return this.hasAnyAuthorityValues[authorities] ?? false;
  }
}
