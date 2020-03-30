//package cn.bytecloud.steam.stats.task;
//
//import cn.bytecloud.steam.constant.ModelConstant;
//import ProjectService;
//import cn.bytecloud.steam.stats.entity.SignUp;
//import cn.bytecloud.steam.stats.entity.Stats;
//import cn.bytecloud.steam.stats.service.StatsService;
//import cn.bytecloud.steam.user.service.UserService;
//import cn.bytecloud.steam.util.UUIDUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Component
//public class StatsTask {
//
//    @Autowired
//    private ProjectService projectService;
//
//    @Autowired
//    private StatsService statsService;
//
//    @Autowired
//    private UserService userService;
//
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void monthTask() throws InterruptedException {
////        Date date = new Date();
////
////        Stats stats = new Stats();
////
////        AtomicInteger docSum = new AtomicInteger();
////        AtomicInteger submitSum = new AtomicInteger();
////
////        projectService.findByDate(getlastDayDate(date), ModelConstant.CREATE_TIME).forEach(project -> {
////            project.getMembers().forEach(member -> {
////                stats.getSignUps().add(new SignUp(project.getId(), project.getCategoryId(), project.getAreaId(),
////                        project.getSchoolId(), project.getGroup(),member.getGender()));
////            });
////
////        });
////        projectService.findByDate(getlastDayDate(date), ModelConstant.PROJECT_DOC_SUBMIT_TIME).forEach(project -> {
////            docSum.addAndGet(1);
////        });
////        projectService.findByDate(getlastDayDate(date), ModelConstant.PROJECT_SUBMIT_TIME).forEach(project -> {
////            submitSum.addAndGet(1);
////        });
////        stats.setDocSum(docSum.get());
////        stats.setSubmitSum(submitSum.get());
////
////        stats.setCreateTime(date.getTime());
////        stats.setUserSum(userService.findByDate(getlastDayDate(date)).size());
////
////        stats.setId(UUIDUtil.getUUID());
////        statsService.save(stats);
//    }
//
//    public Date getlastDayDate(Date date) {
//        if (date == null) {
//            return null;
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.DATE, -1);
//        return calendar.getTime();
//
//    }
//}
