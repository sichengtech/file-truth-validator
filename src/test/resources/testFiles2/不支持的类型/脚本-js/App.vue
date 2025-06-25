<script>
	import nt from '@/utils/native/nativeApp.js'
	export default {
		onLaunch: function() {
			//app设置竖屏锁定
			plus.screen.lockOrientation('portrait-primary');
			nt.setJumpListener((path) => {
				uni.navigateTo({
					url: path
				})
			})
			//获取当前用户登录状态
			if (!this.$store.getters.currentUserLogin) {
				uni.reLaunch({
					url: '/pages/login/index'
				})
			}

		},
		onShow: function() {

		},
		onHide: function() {

		}
	}
</script>
<style lang="scss">
	@import '@/uni_modules/uni-scss/index.scss';
	@import '@/common/layout.scss';
	@import '@/common/mon.scss';
</style>