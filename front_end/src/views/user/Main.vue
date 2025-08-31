<template>
    <div class="app-container">
        <!-- 顶部导航栏 -->
        <header class="app-header">
            <div class="header-content">
                <div class="main-nav">
                    <Logo class="logo" :logoSize="26" :fontSize="20" textColor="rgb(51,51,51)" sysName="个人健康系统" />
                </div>
                <nav class="main-nav">
                    <router-link v-for="item in navItems" :key="item.path" :to="item.path" class="nav-item"
                        active-class="active">
                        {{ item.title }}
                    </router-link>
                    <el-dropdown trigger="click">
                        <span class="user-dropdown-trigger">
                            <div class="user-info">
                                <img :src="userInfo.avatar" alt="用户头像" class="user-avatar">
                                <span class="username">{{ userInfo.username }}</span>
                                <i style="color: rgb(51,51,51);" class="el-icon-arrow-down dropdown-icon"></i>
                            </div>
                        </span>
                        <el-dropdown-menu slot="dropdown" class="user-dropdown">
                            <el-dropdown-item @click.native="updateUserInfo" icon="el-icon-user-solid">
                                修改信息
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="dialogPasswordOperation = true" icon="el-icon-finished">
                                修改密码
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="dialogOutOperation = true" icon="el-icon-s-fold">
                                退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </nav>
            </div>
        </header>

        <!-- 主内容区 -->
        <main class="app-main">
            <router-view></router-view>
        </main>

        <!-- 退出登录弹窗 -->
        <el-dialog title="退出登录" :show-close="false" :visible.sync="dialogOutOperation" width="20%">
            <span>确定退出登录？</span>
            <span slot="footer" class="dialog-footer">
                <span class="primary-bt" @click="dialogOutOperation = false">取消</span>
                <span class="info-bt" @click="confirmLoginOut">确定</span>
            </span>
        </el-dialog>

        <!-- 修改密码弹窗 -->
        <el-dialog title="修改密码" :closeOnClickModal="false" :show-close="false" :visible.sync="dialogPasswordOperation"
            width="28%">
            <div>
                <div>
                    <p>*原始密码</p>
                    <el-input show-password placeholder="输入" type="password" v-model="oldPassword" clearable />
                </div>
                <div>
                    <p>*新密码</p>
                    <el-input show-password placeholder="输入" type="password" v-model="newPassword" clearable />
                </div>
                <div>
                    <p>*确认密码</p>
                    <el-input show-password placeholder="输入" type="password" v-model="againPassword" clearable />
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <span class="primary-bt" @click="dialogPasswordOperation = false">取消</span>
                <span class="info-bt" @click="confirmUpdatePassword">确定</span>
            </span>
        </el-dialog>

        <!-- 用户信息操作弹窗 -->
        <el-dialog title="修改信息" :show-close="false" :visible.sync="dialogUserInfoVisible" :closeOnClickModal="false"
            width="35%">
            <el-tabs v-model="tabActiveName" :tab-position="tabPosition" style="height: 420px;">
                <el-tab-pane label="核心信息" name="first">
                    <div>
                        <div class="user-avatar-container">
                            <p>点击📷处即可上传头像</p>
                            <img v-if="avatar" :src="avatar" alt="用户头像" class="user-avatar-large">
                            <el-upload class="avatar-uploader"
                                action="http://localhost:21090/api/v1.0/self-health-api/file/upload" :show-file-list="false"
                                :on-success="handleImageSuccess">
                                <i class="el-icon-camera-solid upload-icon"></i>
                            </el-upload>
                        </div>
                        <div class="form-group">
                            <p>*用户账号</p>
                            <el-input disabled placeholder="输入" v-model="apiParam.account" clearable />
                        </div>
                        <div class="form-group">
                            <p>*用户名</p>
                            <el-input placeholder="输入" v-model="apiParam.username" clearable />
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="基本信息" name="second">
                    <div>
                        <div class="form-group">
                            <p>用户性别</p>
                            <Tab :buttons="[
                                { label: '女', value: '1' },
                                { label: '男', value: '2' }
                            ]" :initialActive="String(apiParam.gender) || '2'" @change="handleGenderChange" />
                        </div>
                        <div class="form-group">
                            <p>电子邮件</p>
                            <el-input placeholder="输入" v-model="apiParam.email" clearable />
                        </div>
                        <div class="form-group">
                            <p>联系电话</p>
                            <el-input placeholder="输入" v-model="apiParam.phone" clearable />
                        </div>
                        <div class="form-group">
                            <p>出生年月</p>
                            <el-date-picker style="width: 100%;" v-model="apiParam.birthday" type="date"
                                placeholder="选择日期" />
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
            <span slot="footer" class="dialog-footer">
                <span class="primary-bt" @click="dialogUserInfoVisible = false">取消</span>
                <span class="info-bt" @click="handleConfirm">确定修改</span>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import Logo from "@/components/Logo"
import Tab from "@/components/Tab"
import { setUserInfo, getUserInfo, clearToken, clearRole } from "@/utils/storage"

export default {
    components: { Logo, Tab },
    name: "AppLayout",
    data() {
        return {
            oldPassword: '',
            newPassword: '',
            againPassword: '',
            tabActiveName: 'first',
            tabPosition: 'right',
            dialogPasswordOperation: false,
            dialogUserInfoVisible: false,
            dialogOutOperation: false,
            navItems: [
                { path: '/home', title: '首页' },
            ],
            userInfo: {},
            isAuthChecked: false,
            apiParam: {},
            avatar: ''
        }
    },
    async created() {
        await this.handleAuthentication()
        if (this.isAuthChecked) {
            this.initRouteHandling()
        }
    },
    methods: {
        handleGenderChange(obj) {
            this.apiParam.gender = Number(obj.value);
        },
        handleImageSuccess(res) {
            this.$notify({
                title: '头像上传',
                type: res.code === 200 ? 'success' : 'error',
                message: res.code === 200 ? '上传成功' : res.data,
                position: 'bottom-right',
                duration: 1000,
            })
            if (res.code === 200) {
                this.avatar = res.data;
            }
        },
        updateUserInfo() {
            this.avatar = this.userInfo.avatar;
            this.dialogUserInfoVisible = true;
        },
        async handleConfirm() {
            try {
                this.apiParam.avatar = this.avatar;
                const { data, message } = await this.$axios.put('/user/update', this.apiParam);
                this.apiParam = data;
                this.$message.success(message);
                this.handleAuthentication();
                this.dialogUserInfoVisible = false;
            } catch (error) {
                this.$message.warning(error.message);
            }
        },
        async confirmUpdatePassword() {
            if (!this.oldPassword || !this.newPassword || !this.againPassword) {
                this.$message('原始密码、新密码、确认密码不能为空哦');
                return;
            }
            if (this.newPassword !== this.againPassword) {
                this.$message('前后密码输入不一致');
                return;
            }
            const updatePasswordDto = {
                oldPassword: this.$md5(this.$md5(this.oldPassword)),
                newPassword: this.$md5(this.$md5(this.newPassword)),
                againPassword: this.$md5(this.$md5(this.againPassword))
            }
            try {
                const { message } = await this.$axios.put('/user/updatePassword', updatePasswordDto);
                this.$notify.success({
                    title: '密码修改',
                    message: message,
                    duration: 1000,
                    position: 'bottom-right'
                })
                this.confirmLoginOut();
            } catch (error) {
                this.$message.error(error.message);
            }
        },
        confirmLoginOut() {
            clearToken();
            clearRole();
            this.$router.push('/');
        },
        async handleAuthentication() {
            if (this.$route.path === '/login') {
                this.isAuthChecked = true;
                return;
            }

            try {
                const localUser = getUserInfo();
                if (localUser && localUser.token) {
                    this.userInfo = localUser;
                    this.isAuthChecked = true;
                    return;
                }

                const data = await this.$axios.get('/user/auth');
                if (data.code === 200) {
                    this.userInfo = data.data;
                    this.apiParam = { ...data.data };
                    setUserInfo(this.userInfo);
                    this.isAuthChecked = true;
                } else {
                    this.redirectToLogin();
                }
            } catch (error) {
                console.error('认证失败:', error);
                this.redirectToLogin();
            }
        },
        initRouteHandling() {
            this.setupRouteWatcher();
            if (!this.isValidRoute(this.$route.path)) {
                this.$router.replace('/home');
            }
            if (this.$route.path === '/') {
                this.$router.replace('/home');
            }
        },
        isValidRoute(path) {
            return this.navItems.some(item => item.path === path);
        },
        setupRouteWatcher() {
            this.$router.afterEach((to) => {
                if (this.isValidRoute(to.path)) {
                    // 可以在这里记录访问路径
                }
            });
        },
        redirectToLogin() {
            if (this.$route.path !== '/login') {
                this.$notify({
                    type: 'error',
                    title: '安全认证',
                    message: '请重新登录',
                    duration: 2000,
                    position: 'bottom-right'
                });
                this.$router.replace('/login');
            }
        }
    }
}
</script>

<style scoped lang="scss">
.app-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.app-header {
    background-color: rgb(255, 255, 255);
    margin-bottom: 10px;
    z-index: 1000;
    position: sticky;
    top: 0;
    // box-shadow: 0 4px 8px rgb(230, 230, 230);
}

.header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 0 auto;
    padding: 0 50px;
    height: 55px;
    position: relative;
}

.logo {
    display: flex;
    align-items: center;
    cursor: pointer;
}

.main-nav {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 26px;
}

.nav-item {
    color: rgb(51, 51, 51);
    font-size: 16px;
    text-decoration: none;
    height: 74px;
    line-height: 74px;
    white-space: nowrap;
    display: flex;
    align-items: center;
    justify-content: center;

    &.active {
        color: rgb(31, 31, 31);
    }
}

.user-dropdown-trigger {
    cursor: pointer;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-left: 16px;
    cursor: pointer;
    padding: 8px 16px;
    border-radius: 20px;
    background-color: rgba(255, 255, 255, 0.15);
}

.user-avatar {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    object-fit: cover;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.username {
    font-size: 14px;
    color: rgb(51, 51, 51);
    font-weight: 500;
}

.dropdown-icon {
    font-size: 12px;
    margin-left: 4px;
}

.user-dropdown {
    border-radius: 8px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
    padding: 8px 0;
}

.app-main {
    flex: 1;
    box-sizing: border-box;
    border-radius: 5px;
    padding-inline: 20px;
}

.user-avatar-container {
    text-align: center;
    margin-bottom: 30px;

    p {
        font-size: 14px;
        color: #6b7280;
        margin-bottom: 15px;
    }
}

.user-avatar-large {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    object-fit: cover;
    margin-bottom: 15px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.avatar-uploader {
    display: inline-block;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: rgba(21, 85, 154, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    margin-top: -20px;

    i {
        color: #15559a;
        font-size: 18px;
    }
}

.upload-icon {
    color: #15559a;
}

.form-group {
    margin-bottom: 24px;

    p {
        font-size: 14px;
        color: #4b5563;
        margin-bottom: 8px;
        font-weight: 500;
    }

    .el-input {
        width: 100%;
        border-radius: 6px;
    }
}
</style>