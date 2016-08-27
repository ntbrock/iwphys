class IWPProblemServerResponse < ActionWebService::Struct
  member :authkey, :string
  member :directory, :string
  member :files, [:string]
  member :date, :string
end
