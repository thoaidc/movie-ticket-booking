package vn.ptit.moviebooking.movie.constants;

@SuppressWarnings("unused")
public interface DatetimeConstants {

    interface ZoneID {
        String DEFAULT = "UTC";
        String ASIA_HO_CHI_MINH = "Asia/Ho_Chi_Minh"; // Vietnam (UTC+7)
    }

    interface Formatter {
        String DEFAULT = "yyyy-MM-dd HH:mm:ss";
        String UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String E_DD_MMM_YYYY = "E, dd MMM yyyy"; // Example: Thu, 13 Feb 2025
        String YYYY_MM_DD_NORMALIZED = "yyyyMMdd";
        String YYYY_MM_DD_HH_MM_SS_NORMALIZED = "yyyyMMddHHmmss";
        String DD_MM_YYYY_SLASH = "dd/MM/yyyy";
        String YYYY_MM_DD_DASH = "yyyy/MM/dd";
        String DD_MM_YYYY_HH_MM_SS_SLASH = "dd/MM/yyyy HH:mm:ss";
        String DD_MM_YYYY_DASH = "dd-MM-yyyy";
        String YYYY_MM_DD_SLASH = "yyyy-MM-dd";
        String DD_MM_YYYY_HH_MM_SS_DASH = "dd-MM-yyyy HH:mm:ss";
    }
}
