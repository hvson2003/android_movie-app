package com.example.android_ck.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.android_ck.models.PhimVaTheLoai;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.android_ck.models.items.UserItem;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DBName = "app.db";
    private Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String taikhoan = "CREATE TABLE taikhoan(" +
                "tentaikhoan TEXT PRIMARY KEY," +
                "matkhau TEXT," +
                "quyen TEXT," +
                "ngaytao TEXT)";
        db.execSQL(taikhoan);

        // Tạo bảng thông tin cá nhân
        String thongtincanhan = "CREATE TABLE thongtincanhan(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "hoten TEXT," +
                "gioitinh TEXT," +
                "ngaysinh TEXT," +
                "email TEXT," +
                "sdt TEXT," +
                "tentaikhoan TEXT," +
                "FOREIGN KEY(tentaikhoan) REFERENCES taikhoan(tentaikhoan))";
        db.execSQL(thongtincanhan);


        // Tạo bảng Thể loại
        String theloai = "CREATE TABLE theloai(" +
                "matheloai INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tentheloai TEXT)";
        db.execSQL(theloai);

        // Tạo bảng Phim
        String phim = "CREATE TABLE phim(" +
                "maphim INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tenphim TEXT," +
                "anhphim BLOB," +
                "ngaycongchieu TEXT," +
                "mota TEXT," +
                "thoiluong TEXT," +
                "gia INTEGER," +
                "matheloai INTEGER," +
                "FOREIGN KEY(matheloai) REFERENCES theloai(matheloai))";
        db.execSQL(phim);

        //Tạo bảng danh sách yêu thích
        String danhsachyeuthich = "CREATE TABLE danhsachyeuthich(" +
                "iddansach INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tentaikhoan TEXT," +
                "maphim INTEGER," +
                "FOREIGN KEY(tentaikhoan) REFERENCES taikhoan(tentaikhoan)," +
                "FOREIGN KEY(maphim) REFERENCES phim(maphim))";
        db.execSQL(danhsachyeuthich);

        // Tạo bảng Hóa đơn
        String hoadon = "CREATE TABLE hoadon(" +
                "idhoadon INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tentaikhoan TEXT," +
                "maphim INTEGER," +
                "soluong INTEGER," +
                "thanhtien INTEGER," +
                "ngaydat TEXT," +
                "FOREIGN KEY(tentaikhoan) REFERENCES taikhoan(tentaikhoan)," +
                "FOREIGN KEY(maphim) REFERENCES phim(maphim))";
        db.execSQL(hoadon);

        // Tạo bảng giỏ hàng
        String giohang = "CREATE TABLE giohang(" +
                "idgiohang INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tentaikhoan TEXT," +
                "maphim INTEGER," +
                "soluong INTEGER," +
                "thanhtien INTEGER," +
                "FOREIGN KEY(tentaikhoan) REFERENCES taikhoan(tentaikhoan)," +
                "FOREIGN KEY(maphim) REFERENCES phim(maphim))";
        db.execSQL(giohang);

        // Kiểm tra xem có tài khoản admin trong cơ sở dữ liệu hay không
        Cursor cursor = db.rawQuery("SELECT * FROM taikhoan WHERE quyen = ?", new String[]{"admin"});

        if (cursor.getCount() == 0) {
            // Nếu không có, thêm một tài khoản admin mặc định
            ContentValues values = new ContentValues();
            values.put("tentaikhoan", "admin");
            values.put("matkhau", "admin"); // Bạn nên sử dụng mã hóa mật khẩu trong thực tế
            values.put("quyen", "admin");
            values.put("ngaytao", "27/03/2024"); // Ngày tạo tài khoản
            db.insert("taikhoan", null, values);
        }
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop các bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS taikhoan");
        db.execSQL("DROP TABLE IF EXISTS theloai");
        db.execSQL("DROP TABLE IF EXISTS phim");
        db.execSQL("DROP TABLE IF EXISTS danhsachyeuthich");
        db.execSQL("DROP TABLE IF EXISTS hoadon");
        db.execSQL("DROP TABLE IF EXISTS giohang");
        db.execSQL("DROP TABLE IF EXISTS thongtincanhan");

        // Tạo lại các bảng mới
        onCreate(db);
    }


    //Các function liên quan đến thể loại (thêm , sửa, xóa, xem)
    public boolean addGenre(String tentheloai) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tentheloai", tentheloai);
        long result = myDB.insert("theloai", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //kiểm tra tên thể loại đã tồn tại chưa
    public boolean checkGenreExists(String tentheloai) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM theloai WHERE tentheloai = ?", new String[]{tentheloai});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


//    public List<TheLoai> getAllGenreItems() {
//        List<TheLoai> genreList = new ArrayList<>();
//        SQLiteDatabase myDB = this.getReadableDatabase();
//        Cursor cursor = myDB.rawQuery("SELECT * FROM theloai ORDER BY matheloai DESC", null);
//        if (cursor.moveToFirst()) {
//            do {
//                int matheloai = cursor.getInt(0);
//                String tentheloai = cursor.getString(1);
//
//                //Tạo đ tương TheLoai từ dữ liê được thêm vào danh sách
//                genreList.add(new TheLoai(matheloai, tentheloai));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return genreList;
//    }


    public boolean editGenre(int matheloai, String tentheloai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tentheloai", tentheloai);


        int rowsAffected = db.update("theloai", values, "matheloai=?", new String[]{String.valueOf(matheloai)});
        db.close();

        // Trả về true nếu có ít nhất một hàng bị ảnh hưởng
        return rowsAffected > 0;
    }

    public boolean deleteGenre(int matheloai) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("theloai", "matheloai=?", new String[]{String.valueOf(matheloai)});

        // Kiểm tra xem có xóa hết dữ liệu từ bảng không
        if (rowsAffected > 0) {
            // Kiểm tra xem bảng có bản ghi nào không
            Cursor cursor = db.rawQuery("SELECT * FROM SQLITE_SEQUENCE WHERE name='theloai'", null);
            if (cursor.getCount() > 0) {
                // Nếu không có bản ghi, đặt lại chỉ số tự tăng (AUTOINCREMENT)
                db.execSQL("UPDATE SQLITE_SEQUENCE SET seq=0 WHERE name='theloai'");
            }
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

   public boolean themTaikhoan(String tentaikhoan, String matkhau, String ngaytao){
        SQLiteDatabase myDB = this.getWritableDatabase();
        String quyen = "khachhang";
        ContentValues contentValues = new ContentValues();
        contentValues.put("tentaikhoan", tentaikhoan);
        contentValues.put("matkhau",matkhau);
        contentValues.put("quyen",quyen);
        contentValues.put("ngaytao",ngaytao);
        long result = myDB.insert("taikhoan",null,contentValues);
        if(result==-1)return false;
        else return true;
    }

    public boolean ktraTen(String tentaikhoan){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from taikhoan where tentaikhoan = ?", new String[]{tentaikhoan});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public boolean ktraDangnhap(String tentaikhoan,String matkhau){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from taikhoan where tentaikhoan = ? and matkhau = ?", new String[]{tentaikhoan,matkhau});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public boolean themThongTinCaNhan(String hoten, String gioitinh, String ngaysinh, String email, String sdt, String tentakhoan) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("hoten", hoten);
        values.put("gioitinh", gioitinh);
        values.put("ngaysinh", ngaysinh);
        values.put("email", email);
        values.put("sdt", sdt);
        values.put("tentaikhoan", tentakhoan);

        long result = db.insert("thongtincanhan", null, values);
        if(result==-1)return false;
        else return true;
    }

    public boolean ktraQuenmk(String email, String tentaikhoan) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM thongtincanhan WHERE email = ? AND tentaikhoan = ?", new String[]{email, tentaikhoan});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteAllGenres() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("theloai", null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='theloai'");
        db.close();
    }

    //Các function liên quan đến phim


        //lấy dữ liệu tên thể loại từ bảng thể loại để có thể đổ dữ liệu vào spinner
        public List<String> getNameGenre() {
            List<String> genreNames = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM theloai ORDER BY matheloai DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    String genreName = cursor.getString(1);
                    genreNames.add(genreName);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return genreNames;
        }

    public boolean addMovie(String tenphim, byte[] anhphim, String ngaycongchieu, String mota, String thoiluong, int giave, String tentheloai) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Lấy mã thể loại từ tên thể loại được cung cấp
        int matheloai = getGenreId(tentheloai);

        // Nếu không tìm thấy mã thể loại, trả về false
        if (matheloai == -1) {
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("tenphim", tenphim);
        contentValues.put("anhphim", anhphim);
        contentValues.put("ngaycongchieu", ngaycongchieu);
        contentValues.put("mota", mota);
        contentValues.put("thoiluong", thoiluong);
        contentValues.put("gia", giave);
        contentValues.put("matheloai", matheloai);

        long result = db.insert("phim", null, contentValues);
        db.close();

        // Trả về true nếu thêm thành công, ngược lại trả về false
        return result != -1;
    }

    // Phương thức để lấy mã thể loại từ tên thể loại
    public int getGenreId(String tentheloai) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT matheloai FROM theloai WHERE tentheloai = ?", new String[]{tentheloai});
        int matheloai = -1;
        if (cursor.moveToFirst()) {
            matheloai = cursor.getInt(0);
        }
        cursor.close();
        return matheloai;
    }


    public List<PhimVaTheLoai> getAllMoviesWithGenre() {
        List<PhimVaTheLoai> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT phim.maphim, phim.tenphim, phim.anhphim, phim.ngaycongchieu, phim.mota, phim.thoiluong, phim.gia, phim.matheloai, theloai.tentheloai " +
                "FROM phim " +
                "INNER JOIN theloai ON phim.matheloai = theloai.matheloai " +
                "ORDER BY phim.maphim DESC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int maphim = cursor.getInt(0);
                String tenphim = cursor.getString(1);
                byte[] anhphim = cursor.getBlob(2);
                String ngaycongchieu = cursor.getString(3);
                String mota = cursor.getString(4);
                String thoiluong = cursor.getString(5);
                int gia = cursor.getInt(6);
                int matheloai = cursor.getInt(7);
                String tentheloai = cursor.getString(8);

//                PhimVaTheLoai movie = new PhimVaTheLoai(maphim, tenphim, anhphim, ngaycongchieu, mota, thoiluong, gia, matheloai, tentheloai);
//                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movieList;
    }
    //Hàm lấy tên thể loại duy nhất để đổ lên khachhang_recy_theloai
    public List<String> getDistinctGenreNames() {
        List<String> distinctGenreNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT theloai.tentheloai " +
                "FROM theloai " +
                "INNER JOIN phim ON theloai.matheloai = phim.matheloai";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String genreName = cursor.getString(0);
                distinctGenreNames.add(genreName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return distinctGenreNames;
    }



    //Lấy phim giữa trên tên thể loại

    public List<PhimVaTheLoai> getMoviesByGenre(String tentheloai) {
        List<PhimVaTheLoai> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT phim.maphim, phim.tenphim, phim.anhphim, phim.ngaycongchieu, phim.mota, phim.thoiluong, phim.gia, phim.matheloai, theloai.tentheloai " +
                "FROM phim " +
                "INNER JOIN theloai ON phim.matheloai = theloai.matheloai " +
                "WHERE theloai.tentheloai = ?";

        Cursor cursor = db.rawQuery(query, new String[]{tentheloai});
        if (cursor.moveToFirst()) {
            do {
                int maphim = cursor.getInt(0);
                String tenphim = cursor.getString(1);
                byte[] anhphim = cursor.getBlob(2);
                String ngaycongchieu = cursor.getString(3);
                String mota = cursor.getString(4);
                String thoiluong = cursor.getString(5);
                int gia = cursor.getInt(6);
                int matheloai = cursor.getInt(7);
                String genreName = cursor.getString(8);

//                PhimVaTheLoai movie = new PhimVaTheLoai(maphim, tenphim, anhphim, ngaycongchieu, mota, thoiluong, gia, matheloai, genreName);
//                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movieList;
    }



    //Edit phim
    public boolean editMovie(int maphim, String tenphim, byte[] anhphim, String ngaychieu, String mota, String thoiluong, int giave, int matheloai) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenphim", tenphim);
        values.put("anhphim", anhphim);
        values.put("ngaycongchieu", ngaychieu);
        values.put("mota", mota);
        values.put("thoiluong", thoiluong);
        values.put("gia", giave);
        values.put("matheloai", matheloai);
        String[] whereArgs = {String.valueOf(maphim)};
        int result = db.update("phim", values, "maphim = ?", whereArgs);
        db.close();
        return result > 0;
    }

    public boolean deleteFilm(int maphim) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("phim", "maphim=?", new String[]{String.valueOf(maphim)});

        // Kiểm tra xem có xóa hết dữ liệu từ bảng không
        if (rowsAffected > 0) {
            // Kiểm tra xem bảng có bản ghi nào không
            Cursor cursor = db.rawQuery("SELECT * FROM SQLITE_SEQUENCE WHERE name='phim'", null);
            if (cursor.getCount() > 0) {
                // Nếu không có bản ghi, đặt lại chỉ số tự tăng (AUTOINCREMENT)
                db.execSQL("UPDATE SQLITE_SEQUENCE SET seq=0 WHERE name='phim'");
            }
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public void deleteAllFilm() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("phim", null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='phim'");
        db.close();
    }


    public boolean suatMatKhau(String tentaikhoan, String matkhauMoi) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("matkhau", matkhauMoi);

        int rowsUpdated = myDB.update("taikhoan", contentValues, "tentaikhoan = ?", new String[]{tentaikhoan});

        if (rowsUpdated > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean suaThongtincanhan(String hoten, String gioitinh, String ngaysinh, String email, String sdt, String tentaikhoan) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hoten", hoten);
        contentValues.put("gioitinh", gioitinh);
        contentValues.put("ngaysinh", ngaysinh);
        contentValues.put("email", email);
        contentValues.put("sdt", sdt);

        int rowUpdated = myDB.update("thongtincanhan", contentValues, "tentaikhoan = ?", new String[]{tentaikhoan});

        if (rowUpdated > 0) {
            return true;
        } else  {
            return false;
        }
    }

    public List<UserItem> layTatCaThongTinCaNhan() {
        List<UserItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT thongtincanhan.email, taikhoan.tentaikhoan " +
                "FROM thongtincanhan " +
                "INNER JOIN taikhoan ON thongtincanhan.tentaikhoan = taikhoan.tentaikhoan " +
                "WHERE taikhoan.quyen = ?";

        Cursor cursor = db.rawQuery(query, new String[]{"khachhang"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String email = cursor.getString(0);
                String tk = cursor.getString(1);
                list.add(new UserItem(tk, email));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public Cursor layThongTinCaNhan(String tentaikhoan) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery(
                "SELECT * FROM thongtincanhan WHERE tentaikhoan = ?", new String[]{tentaikhoan}
        );
        return cursor;
    }

    public Cursor layThongtintaikhoan(String tentaikhoan) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery(
                "SELECT * FROM taikhoan WHERE tentaikhoan = ?", new String[]{tentaikhoan}
        );
        return cursor;
    }

    public int layTongSoLuongPhimYeuThich(String tentaikhoan) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery(
                "SELECT COUNT(*) FROM danhsachyeuthich WHERE tentaikhoan = ?", new String[]{tentaikhoan}
        );

        int totalCount = 0;
        if (cursor.moveToFirst()) {
            totalCount = cursor.getInt(0);
        }

        cursor.close();
        return totalCount;
    }

    public int layTongThanhTien(String tentaikhoan) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery(
                "SELECT SUM(thanhtien) FROM hoadon WHERE tentaikhoan = ?", new String[]{tentaikhoan}
        );

        int totalAmount = 0;
        if (cursor.moveToFirst()) {
            totalAmount = cursor.getInt(0);
        }

        cursor.close();
        return totalAmount;
    }

    public boolean xoaTaiKhoan(String tentaikhoan) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            // Xóa thông tin cá nhân của tài khoản từ bảng 'thongtincanhan'
            db.delete("thongtincanhan", "tentaikhoan = ?", new String[]{tentaikhoan});
            // Xóa danh sách yêu thích của tài khoản
            db.delete("danhsachyeuthich", "tentaikhoan = ?", new String[]{tentaikhoan});
            // Xóa hóa đơn của tài khoản
            db.delete("hoadon", "tentaikhoan = ?", new String[]{tentaikhoan});
            // Xóa tài khoản từ bảng 'taikhoan'
            int result = db.delete("taikhoan", "tentaikhoan = ?", new String[]{tentaikhoan});

            // Nếu số dòng bị ảnh hưởng bởi lệnh xóa lớn hơn 0, tức là đã xóa thành công
            if (result > 0) {
                return true;
            } else {
                return false;
            }
        } finally {
            // Đóng cơ sở dữ liệu
            db.close();
        }
    }
    public boolean themDanhSachYeuThich(String tentaikhoan, int maphim){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tentaikhoan", tentaikhoan);
        cv.put("maphim", maphim);
        return db.insert("danhsachyeuthich", null, cv) > 0;
    }

    public boolean kiemTraDSYTTonTai(String tentaikhoan, int maphim){
        String query = "SELECT * FROM danhsachyeuthich WHERE tentaikhoan = '" + tentaikhoan + "' AND maphim = " + maphim;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    public boolean xoaKhoiDanhSachYeuThich(String tentaikhoan, int maphim) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("danhsachyeuthich", "tentaikhoan = ? AND maphim = ?",
                new String[]{tentaikhoan, String.valueOf(maphim)}) > 0;
    }

    public Cursor layDuLieuBangDSYT(String tentk){
        String query = "SELECT * FROM danhsachyeuthich dsyt INNER JOIN phim p ON dsyt.maphim = p.maphim INNER JOIN theloai tl ON p.matheloai = tl.matheloai WHERE dsyt.tentaikhoan = '" + tentk + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean themHoaDonMoi(String tentaikhoan, int maphim, int soluong){
        String ngaydat = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tentaikhoan", tentaikhoan);
        cv.put("maphim", maphim);
        cv.put("ngaydat", ngaydat);
        cv.put("soluong", soluong);
        cv.put("thanhtien", layGiaPhim(maphim)*soluong);
        return db.insert("hoadon", null, cv) > 0;
    }

    public int layGiaPhim(int maphim) {
        SQLiteDatabase db = this.getReadableDatabase();
        int gia = 0;
        String sqlQuery = "SELECT gia FROM phim WHERE maphim = " + maphim;
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            gia = cursor.getInt(0);
            cursor.close();
        }
        return gia;
    }


    public Cursor layDuLieuBangHoaDon(){
        String query = "SELECT * FROM hoadon hd INNER JOIN phim p ON hd.maphim = p.maphim INNER JOIN theloai tl ON p.matheloai=tl.matheloai  " +
                "INNER JOIN thongtincanhan ttcn ON hd.tentaikhoan=ttcn.tentaikhoan ORDER BY hd.idhoadon DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor layDuLieuBangHoaDon(String tentk){
        String query = "SELECT * FROM hoadon hd INNER JOIN phim p ON hd.maphim = p.maphim INNER JOIN theloai tl ON p.matheloai=tl.matheloai " +
                "WHERE hd.tentaikhoan = '" + tentk + "'  ORDER BY hd.idhoadon DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public int getTongTienCacHoaDon() {
        SQLiteDatabase db = this.getReadableDatabase();
        int tongtien = 0;
        String sqlQuery = "SELECT sum(thanhtien) FROM hoadon";
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            tongtien = cursor.getInt(0);
            cursor.close();
        }
        return tongtien;
    }

    public int getTongTienCacHoaDon(String tentaikhoan) {
        SQLiteDatabase db = this.getReadableDatabase();
        int tongtien = 0;
        String sqlQuery = "SELECT sum(thanhtien) FROM hoadon WHERE tentaikhoan = '" + tentaikhoan + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            tongtien = cursor.getInt(0);
            cursor.close();
        }
        return tongtien;
    }

    public boolean themVaoGioHang(String tentaikhoan, int maphim){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tentaikhoan", tentaikhoan);
        cv.put("maphim", maphim);
        cv.put("soluong", 1);
        cv.put("thanhtien", layGiaPhim(maphim)*1);
        return db.insert("giohang", null, cv) > 0;
    }

    public boolean capNhatGioHang(String tentaikhoan, int maphim, int soluong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("soluong", soluong);
        cv.put("thanhtien", layGiaPhim(maphim) * soluong);
        String whereClause = "tentaikhoan = ? AND maphim = ?";
        String[] whereArgs = {tentaikhoan, String.valueOf(maphim)};
        int rowsAffected = db.update("giohang", cv, whereClause, whereArgs);
        return rowsAffected > 0;
    }

    public boolean kiemTraSPGioHang(String tentaikhoan, int maphim){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM giohang WHERE tentaikhoan = '" + tentaikhoan + "' AND maphim = " + maphim;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public Cursor layDuLieuBangGioHang(String tentk){
        String query = "SELECT * FROM giohang gh INNER JOIN phim p ON gh.maphim = p.maphim WHERE gh.tentaikhoan = '" + tentk + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean xoaKhoiGioHang(String tentaikhoan, int maphim) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("giohang", "tentaikhoan = ? AND maphim = ?",
                new String[]{tentaikhoan, String.valueOf(maphim)}) > 0;
    }

    public void xoaGioHang(String tentaikhoan) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("giohang", "tentaikhoan = ?", new String[]{tentaikhoan});
    }

    public boolean kiemTraYeuThichNguoiDung(String tentaikhoan, int maphim){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM danhsachyeuthich WHERE tentaikhoan = '" + tentaikhoan + "' AND maphim = " + maphim;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public int getTongTienGioHang(String tentaikhoan) {
        SQLiteDatabase db = this.getReadableDatabase();
        int tongtien = 0;
        String sqlQuery = "SELECT sum(gh.soluong*p.gia) FROM giohang gh INNER JOIN phim p ON gh.maphim = p.maphim WHERE gh.tentaikhoan = '" + tentaikhoan + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            tongtien = cursor.getInt(0);
            cursor.close();
        }
        return tongtien;
    }

    public Cursor layPhimVaThongTinDoanhSo(){
        String query = "SELECT p.maphim, p.anhphim,p.tenphim, sum(soluong) as TongSoLuong, p.gia, sum(thanhtien) as ThanhTien FROM hoadon hd " +
                "INNER JOIN phim p ON hd.maphim = p.maphim GROUP BY p.maphim;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean kiemTraThongTinCaNhan(String tentaikhoan) {
        String query = "SELECT hoten, sdt FROM thongtincanhan WHERE tentaikhoan = '" + tentaikhoan + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            String hoten = cursor.getString(0);
            String sodienthoai = cursor.getString(1);
            cursor.close();

            if (hoten.equals("Tài khoản chưa có thông tin") || sodienthoai.equals("Đang cập nhật..."))
                return false;
            else
                return true;
        } else {
            return false;
        }
    }


    public static class FirebaseHelper {
    }
}
