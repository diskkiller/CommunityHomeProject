package com.huaxixingfu.sqj.http.api.msg;

import com.diskkiller.http.config.IRequestApi;
import com.huaxixingfu.sqj.http.api.API;

import java.io.Serializable;
import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 社工
 */
public final class ResidentListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.MSG_RESIDENT_LIST;
    }


    /**
     * userId	integer($int64)
     * title: 用户id
     * residentName	string
     * title: 姓名
     * residentNickName	string
     * title: 昵称
     * residentAvatarUrl	string
     * title: 头像
     * zoneRoomAddr	string
     * title: 住址
     * workerPrecinctVOList	辖区详情返回参数[
     * title: 辖区详情返回参数
     * 辖区详情返回参数{
     * type	integer($int32)
     * title: 辖区类型
     * flag	integer($int32)
     * title: 是否已被选择
     * id	integer($int64)
     * title: id
     * name	string
     * title: 辖区名称
     * workerName	string
     * title: 专属社工名称
     * version	integer($int32)
     * title: 版本号
     * children	子类[
     * title: 子类
     * {
     * }]
     * }]
     * workerGroupTreeVOList	社工组返回参数[
     * title: 社工组返回参数
     * 社工组返回参数{
     * flag	integer($int32)
     * title: 是否已被选择
     * roleId	integer($int64)
     * title: 社工组id
     * roleName	string
     * title: 社工组名称
     * version	integer($int32)
     * title: 版本号
     * }]
     * zoneVOTreeVOList	社区返回参数[
     * title: 社区返回参数
     * 社区返回参数{
     * flag	integer($int32)
     * title: 是否已被选择
     * zoneId	integer($int64)
     * title: 社区id
     * zoneName	string
     * title: 社区名称
     * version	integer($int32)
     * title: 版本号
     * }]
     * flag	boolean
     * title: 是否在群
     * userPhone	string
     * title: 手机号
     * userStatus	integer($int32)
     * title: 账号状态code
     * userStatusName	string
     * title: 账号状态
     * userUid	string
     * title: uid
     */
    public final static class Bean implements Serializable {
        private int userId;
        private String residentName;
        private String residentNickName;
        private String residentAvatarUrl;
        private String zoneRoomAddr;
        private List<WorkerPrecinctVOList> workerPrecinctVOList;
        private List<WorkerGroupTreeVOList> workerGroupTreeVOList;
        private List<ZoneVOTreeVOList> zoneVOTreeVOList;
        private boolean flag;
        private String userPhone;
        private int userStatus;
        private String userStatusName;
        private String userUid;
        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setResidentName(String residentName) {
            this.residentName = residentName;
        }
        public String getResidentName() {
            return residentName;
        }

        public void setResidentNickName(String residentNickName) {
            this.residentNickName = residentNickName;
        }
        public String getResidentNickName() {
            return residentNickName;
        }

        public void setResidentAvatarUrl(String residentAvatarUrl) {
            this.residentAvatarUrl = residentAvatarUrl;
        }
        public String getResidentAvatarUrl() {
            return residentAvatarUrl;
        }

        public void setZoneRoomAddr(String zoneRoomAddr) {
            this.zoneRoomAddr = zoneRoomAddr;
        }
        public String getZoneRoomAddr() {
            return zoneRoomAddr;
        }

        public void setWorkerPrecinctVOList(List<WorkerPrecinctVOList> workerPrecinctVOList) {
            this.workerPrecinctVOList = workerPrecinctVOList;
        }
        public List<WorkerPrecinctVOList> getWorkerPrecinctVOList() {
            return workerPrecinctVOList;
        }

        public void setWorkerGroupTreeVOList(List<WorkerGroupTreeVOList> workerGroupTreeVOList) {
            this.workerGroupTreeVOList = workerGroupTreeVOList;
        }
        public List<WorkerGroupTreeVOList> getWorkerGroupTreeVOList() {
            return workerGroupTreeVOList;
        }

        public void setZoneVOTreeVOList(List<ZoneVOTreeVOList> zoneVOTreeVOList) {
            this.zoneVOTreeVOList = zoneVOTreeVOList;
        }
        public List<ZoneVOTreeVOList> getZoneVOTreeVOList() {
            return zoneVOTreeVOList;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
        public boolean getFlag() {
            return flag;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }
        public String getUserPhone() {
            return userPhone;
        }

        public void setUserStatus(int userStatus) {
            this.userStatus = userStatus;
        }
        public int getUserStatus() {
            return userStatus;
        }

        public void setUserStatusName(String userStatusName) {
            this.userStatusName = userStatusName;
        }
        public String getUserStatusName() {
            return userStatusName;
        }

        public void setUserUid(String userUid) {
            this.userUid = userUid;
        }
        public String getUserUid() {
            return userUid;
        }



        public static final class WorkerPrecinctVOList implements Serializable{

            private int type;
            private int flag;
            private int id;
            private String name;
            private String workerName;
            private int version;
            //private List<String> children;
            public void setType(int type) {
                this.type = type;
            }
            public int getType() {
                return type;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }
            public int getFlag() {
                return flag;
            }

            public void setId(int id) {
                this.id = id;
            }
            public int getId() {
                return id;
            }

            public void setName(String name) {
                this.name = name;
            }
            public String getName() {
                return name;
            }

            public void setWorkerName(String workerName) {
                this.workerName = workerName;
            }
            public String getWorkerName() {
                return workerName;
            }

            public void setVersion(int version) {
                this.version = version;
            }
            public int getVersion() {
                return version;
            }

           /* public void setChildren(List<String> children) {
                this.children = children;
            }
            public List<String> getChildren() {
                return children;
            }*/

        }



        public static final class WorkerGroupTreeVOList  implements Serializable{

            private int flag;
            private int roleId;
            private String roleName;
            private int version;
            public void setFlag(int flag) {
                this.flag = flag;
            }
            public int getFlag() {
                return flag;
            }

            public void setRoleId(int roleId) {
                this.roleId = roleId;
            }
            public int getRoleId() {
                return roleId;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }
            public String getRoleName() {
                return roleName;
            }

            public void setVersion(int version) {
                this.version = version;
            }
            public int getVersion() {
                return version;
            }

        }



        public static final class ZoneVOTreeVOList implements Serializable {

            private int flag;
            private int zoneId;
            private String zoneName;
            private int version;
            public void setFlag(int flag) {
                this.flag = flag;
            }
            public int getFlag() {
                return flag;
            }

            public void setZoneId(int zoneId) {
                this.zoneId = zoneId;
            }
            public int getZoneId() {
                return zoneId;
            }

            public void setZoneName(String zoneName) {
                this.zoneName = zoneName;
            }
            public String getZoneName() {
                return zoneName;
            }

            public void setVersion(int version) {
                this.version = version;
            }
            public int getVersion() {
                return version;
            }

        }
    }
}