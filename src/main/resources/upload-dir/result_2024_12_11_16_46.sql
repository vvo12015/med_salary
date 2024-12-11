USE [DoctorEleks v1]
GO

INSERT INTO [dbo].[Premium](
[sl_id]
, [UserShortName]
, [DepartmentName]
, [DepartmentID]
, [UserPosition]
, [EmploymentStartDate]
, [Employment]
, [EmploymentPart]
, [EmploymentUserPositionPart]
, [HourCoefficient]
, [NightHours]
, [Point]
, [PointValue]
, [BasicPremium]
, [HospNSZU_Premium]
, [CountEMR_stationary]
, [AmblNSZU_Premium]
, [SumForAmlPackage]
, [CountEMR_priorityService]
, [OneDaySurgery]
, [CountEMR_oneDaySurgery]
, [OtherPremium]
, date)
SELECT '2159', N'Ігнатишак Олександра Михайлівна', N'Інфекційне відділення', 3, N'лікар-інфекціоніст дитячий', '1998-12-01', 0,25, 0,25, 1,00, 1.0, 16.0, 0, 0, 720.0, 0.0, 0, 0.0, 0.0, 0, 0.0, 0, 720.0, GETDATE()
UNION ALL 
SELECT '845', N'Ігнатишак Олександра Михайлівна', N'Інфекційне відділення', 3, N'лікар-інфекціоніст', '1998-12-01', 0,75, 0,75, 1,00, 1.0, 40.0, 0, 0, 2605.3013, 445.3012, 8, 0.0, 0.0, 0, 0.0, 0, 2160.0, GETDATE()
UNION ALL 
