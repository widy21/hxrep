/**
 * 工具模块
 */
var Utils = {};
Utils.Constants = {
    ONE_DAY: 86400000, // 1000*60*60*24
    DATE_REGEXP: /^20\d{2}-\d{2}-\d{2}/
};


/**
 * 时间工具
 */
Utils.DateUtils = {
    /**
     * 两个时间的间隔，单位为天，注意若start，比end还晚，那就是负值
     *
     * @param start
     *            日期字符串，形如“yyyy-mm-dd”
     * @param end
     *            日期字符串，形如“yyyy-mm-dd”
     * @returns {Number}
     */
    daysBetween: function (start, end) {
        if (!Utils.Constants.DATE_REGEXP.test(start)) {
            return null;
        }
        if (!Utils.Constants.DATE_REGEXP.test(end)) {
            return null;
        }
        var startArr = start.split("-"), endArr = end.split("-"), startDate = new Date(
            startArr[0], startArr[1] - 1, startArr[2]), endDate = new Date(
            endArr[0], endArr[1] - 1, endArr[2]), milliseconds = endDate
                .getTime()
            - startDate.getTime(), days = milliseconds
            / Utils.Constants.ONE_DAY;

        return days;
    },
    /**
     * 在指定日期（字符串形式：“yyyy-mm-dd”）上加上指定天数后，返回新的的时间字符串
     *
     * @param dateStr
     * @param dayNum
     * @returns {String}
     */
    plusDayNum: function (datestr, dayNum) {
        if (!Utils.Constants.DATE_REGEXP.test(datestr)) {
            return null;
        }
        var array = datestr.split("-");

        var date = new Date(array[0], array[1] - 1, array[2]), milliseconds = dayNum
            * Utils.Constants.ONE_DAY;
        newDateMilliseconds = milliseconds + date.getTime(),
            newDate = new Date(), newDate.setTime(newDateMilliseconds),
            month = newDate.getMonth() + 1, day = newDate.getDate();
        if (month < 10) {
            month = "0" + month;
        }
        if (day < 10) {
            day = "0" + day;
        }
        return newDate.getFullYear() + "-" + month + "-" + day;
    },
    /**
     * 将日期转换为统一的String格式
     *
     * @param date
     * @returns {String}
     */
    dateToString: function (date) {
        if (date) {
            var year = date.getFullYear(), month = date.getMonth() + 1, day = date
                .getDate();
            if (month < 10) {
                month = "0" + month;
            }
            if (day < 10) {
                day = "0" + day;
            }
            return year + "-" + month + "-" + day;
        }
        return null;
    },
    /**
     * 将日期字符串转换为日期对象，要求日期字符串为yyyy-mm-dd格式
     *
     * @param date
     * @returns {String}
     */
    stringToDate: function (str) {
        if (!Utils.Constants.DATE_REGEXP.test(str)) {
            return null;
        }
        return new Date(str.replace(/-/g, "/"));
    },
    /**
     * 获取指定日期所在星期的周一，返回字符串
     *
     * @param theDate
     * @returns {String}
     */
    firstDateOfWeek: function (theDate) {
        var firstDateOfWeek, dayOfWeek = theDate.getDay();
        // 如果是星期天
        if (dayOfWeek == 0) {
            firstDateOfWeek = this.plusDayNum(this.dateToString(theDate), -6);
        } else {
            firstDateOfWeek = this.plusDayNum(this.dateToString(theDate),
                -(dayOfWeek - 1));
        }
        return firstDateOfWeek;
    },
    /**
     * 获取指定日期所在星期的周日，返回字符串
     *
     * @param theDate
     * @returns {String}
     */
    lastDateOfWeek: function (theDate) {
        // 先得到周一，再加6
        return this.plusDayNum(this.firstDateOfWeek(theDate), 6);
    },
    /**
     * 获取指定日期所在月的第一天
     * @param theDate
     * @returns {String}
     */
    firstDateOfMonth: function (theDate) {
        theDate.setDate(1); //第一天
        var endDate = new Date(theDate);
        var month_first = new XDate(theDate).toString('yyyy-MM-dd');
        return month_first;
    },
    /**
     * 获取指定日期所在月的最后一天
     * @param theDate
     * @returns {String}
     */
    endDateOfMonth: function (theDate) {
        theDate.setDate(1); //第一天
        var endDate = new Date(theDate);
        endDate.setMonth(theDate.getMonth()+1);
        endDate.setDate(0);
        var month_end = new XDate(endDate).toString('yyyy-MM-dd');
        return month_end;
    },
    /**
     * 获取指定日期偏移指定月数后的日期
     * @param theDate
     * @returns {String}
     */
    plumMonthOfTheDate: function (theDate,monthNum) {
        var endDate = new Date(theDate);
        endDate.setMonth(theDate.getMonth()+monthNum);
        //endDate.setDate(0);
        var result_date = new XDate(endDate).toString('yyyy-MM-dd');
        return result_date;
    }
};